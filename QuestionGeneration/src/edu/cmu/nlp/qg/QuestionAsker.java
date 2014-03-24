package edu.cmu.nlp.qg;

import java.util.*;
import edu.stanford.nlp.trees.Tree;

/**
 * Wrapper class for outputting a (ranked) list of questions given an entire
 * document, not just a sentence. It wraps the three stages discussed in the
 * technical report and calls each in turn (along with parsing and other
 * preprocessing) to produce questions.
 * 
 * This is the typical class to use for running the system via the command line.
 * 
 * Example usage:
 * 
 * java -server -Xmx800m -cp
 * lib/weka-3-6.jar:lib/stanford-parser-2008-10-26.jar:
 * bin:lib/jwnl.jar:lib/commons
 * -logging.jar:lib/commons-lang-2.4.jar:lib/supersense
 * -tagger.jar:lib/stanford-ner-2008-05-07.jar:lib/arkref.jar \
 * edu/cmu/ark/QuestionAsker \ --verbose --simplify --group \ --model
 * models/linear-regression-ranker-06-24-2010.ser.gz \ --prefer-wh --max-length
 * 30 --downweight-pro
 * 
 * @author mheilman@cs.cmu.edu
 * 
 */
public class QuestionAsker {

	public QuestionAsker() {
	}

	private static QuestionTransducer qt = new QuestionTransducer();
	private static InitialTransformationStep trans = new InitialTransformationStep();
	private static QuestionRanker qr = null;

	private static boolean preferWH = false;
	private static boolean doNonPronounNPC = false;
	private static boolean doPronounNPC = true;
	private static Integer maxLength = 1000;
	private static boolean downweightPronouns = false;
	private static boolean avoidFreqWords = false;
	private static boolean dropPro = true;
	private static boolean justWH = false;
	private static List<Question> outputQuestionList = new ArrayList<Question>();

	public static void initialize(String[] args) {
		qt.setAvoidPronounsAndDemonstratives(false);
		AnalysisUtilities.getInstance();
		String modelPath = null;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("--debug")) {
				GlobalProperties.setDebug(true);
			} else if (args[i].equals("--verbose")) {
			} else if (args[i].equals("--model")) { // ranking model path
				modelPath = args[i + 1];
				i++;
			} else if (args[i].equals("--keep-pro")) {
				dropPro = false;
			} else if (args[i].equals("--downweight-pro")) {
				dropPro = false;
				downweightPronouns = true;
			} else if (args[i].equals("--downweight-frequent-answers")) {
				avoidFreqWords = true;
			} else if (args[i].equals("--properties")) {
				GlobalProperties.loadProperties(args[i + 1]);
			} else if (args[i].equals("--prefer-wh")) {
				preferWH = true;
			} else if (args[i].equals("--just-wh")) {
				justWH = true;
			} else if (args[i].equals("--full-npc")) {
				doNonPronounNPC = true;
			} else if (args[i].equals("--no-npc")) {
				doPronounNPC = false;
			} else if (args[i].equals("--max-length")) {
				maxLength = new Integer(args[i + 1]);
				i++;
			}
		}

		qt.setAvoidPronounsAndDemonstratives(dropPro);
		trans.setDoPronounNPC(doPronounNPC);
		trans.setDoNonPronounNPC(doNonPronounNPC);

		if (modelPath != null) {
			System.err.println("Loading question ranking models from "
					+ modelPath + "...");
			qr = new QuestionRanker();
			qr.loadModel(modelPath);
		}
	}

	public static HashMap<String, Double> askQuestion(String doc) {

		HashMap<String, Double> questionScore = new HashMap<String, Double>();
		Tree parsed;

		try {
			outputQuestionList.clear();

			List<String> sentences = AnalysisUtilities.getSentences(doc);

			// iterate over each segmented sentence and generate questions
			List<Tree> inputTrees = new ArrayList<Tree>();

			for (String sentence : sentences) {
				if (GlobalProperties.getDebug())
					System.err.println("Question Asker: sentence: " + sentence);

				parsed = AnalysisUtilities.getInstance()
						.parseSentence(sentence).parse;
				inputTrees.add(parsed);
			}

			// step 1 transformations
			List<Question> transformationOutput = trans.transform(inputTrees);

			// step 2 question transducer
			for (Question t : transformationOutput) {
				if (GlobalProperties.getDebug())
					System.err.println("Stage 2 Input: "
							+ t.getIntermediateTree().yield().toString());
				qt.generateQuestionsFromParse(t);
				outputQuestionList.addAll(qt.getQuestions());
			}

			// remove duplicates
			QuestionTransducer.removeDuplicateQuestions(outputQuestionList);

			// step 3 ranking
			if (qr != null) {
				qr.scoreGivenQuestions(outputQuestionList);
				boolean doStemming = true;
				QuestionRanker.adjustScores(outputQuestionList, inputTrees,
						avoidFreqWords, preferWH, downweightPronouns,
						doStemming);
				QuestionRanker.sortQuestions(outputQuestionList, false);
			}

			// now print the questions
			// double featureValue;
			for (Question question : outputQuestionList) {
				if (question.getTree().getLeaves().size() > maxLength) {
					continue;
				}
				if (justWH && question.getFeatureValue("whQuestion") != 1.0) {
					continue;
				}
				questionScore.put(question.yield(), question.getScore());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return questionScore;
	}
	
	public static void printFeatureNames() {
		List<String> featureNames = Question.getFeatureNames();
		for (int i = 0; i < featureNames.size(); i++) {
			if (i > 0) {
				System.out.print("\n");
			}
			System.out.print(featureNames.get(i));
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		String[] params = "--model models/linear-regression-ranker-reg500.ser.gz --prefer-wh --max-length 30 --downweight-pro"
				.split("  *|		*");
		QuestionAsker.initialize(params);
		HashMap<String, Double> questionScore = QuestionAsker.askQuestion("Richard likes Lily.");
		System.out.println(questionScore);
		questionScore = QuestionAsker.askQuestion("William Cohen is a professor at CMU.");
		System.out.println(questionScore);
	}
}
