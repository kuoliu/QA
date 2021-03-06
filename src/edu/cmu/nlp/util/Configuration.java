package edu.cmu.nlp.util;

public class Configuration {

	//Configuration for wordnet part
	private static String wordnetDatabasePath = "./files/dict/";
	private static String wordnetPathForSim = "./files";
	
	//Configuration for solr part
	private static int TOP_SEARCH_RESULTS = 10;
	private static String solrServerUrl = "http://localhost:8983/solr";
	private static String solrSchemaName = "11611-project";
	private static String solrCoreName = "";

	//QC
	private static int hypernymLevel = 3;
	private static String questionGeneralDescPath = "files/qc/question.general.desc";
	private static String questionDeepDescPath = "files/qc/question.deep.desc";
	private static String questionTrainPath = "result/question_class";
	private static String questionTestPath = "result/question_class";
	private static String questionClassPath = "result/question_class";
	private static String smoOption = "";
	
	//other
	private static String wordListPath = "files/wordlist";
	private static String pronounListPath = "files/pronoun";
	
	public static String getWordnetDatabasePath() {
		return wordnetDatabasePath;
	}

	public static void setWordnetDatabasePath(String wordnetDatabasePath) {
		Configuration.wordnetDatabasePath = wordnetDatabasePath;
	}

	public static String getWordnetPathForSim() {
		return wordnetPathForSim;
	}

	public static void setWordnetPathForSim(String wordnetPathForSim) {
		Configuration.wordnetPathForSim = wordnetPathForSim;
	}

	public static int getTOP_SEARCH_RESULTS() {
		return TOP_SEARCH_RESULTS;
	}

	public static void setTOP_SEARCH_RESULTS(int tOP_SEARCH_RESULTS) {
		TOP_SEARCH_RESULTS = tOP_SEARCH_RESULTS;
	}

	public static String getSolrServerUrl() {
		return solrServerUrl;
	}

	public static void setSolrServerUrl(String solrServerUrl) {
		Configuration.solrServerUrl = solrServerUrl;
	}

	public static String getSolrSchemaName() {
		return solrSchemaName;
	}

	public static void setSolrSchemaName(String solrSchemaName) {
		Configuration.solrSchemaName = solrSchemaName;
	}

	public static String getSolrCoreName() {
		return solrCoreName;
	}

	public static void setSolrCoreName(String solrCoreName) {
		Configuration.solrCoreName = solrCoreName;
	}

	public static int getHypernymLevel() {
		return hypernymLevel;
	}

	public static void setHypernymLevel(int hypernymLevel) {
		Configuration.hypernymLevel = hypernymLevel;
	}

	public static String getQuestionGeneralDescPath() {
		return questionGeneralDescPath;
	}

	public static void setQuestionGeneralDescPath(
			String questionGeneralDescPath) {
		Configuration.questionGeneralDescPath = questionGeneralDescPath;
	}

	public static String getQuestionDeepDescPath() {
		return questionDeepDescPath;
	}

	public static void setQuestionDeepDescPath(String questionDeepDescPath) {
		Configuration.questionDeepDescPath = questionDeepDescPath;
	}

	public static String getWordListPath() {
		return wordListPath;
	}

	public static void setWordListPath(String wordListPath) {
		Configuration.wordListPath = wordListPath;
	}

	public static String getQuestionClassPath() {
		return questionClassPath;
	}

	public static void setQuestionClassPath(String questionClassPath) {
		Configuration.questionClassPath = questionClassPath;
	}

	public static String getQuestionTrainPath() {
		return questionTrainPath;
	}

	public static void setQuestionTrainPath(String questionTrainPath) {
		Configuration.questionTrainPath = questionTrainPath;
	}

	public static String getQuestionTestPath() {
		return questionTestPath;
	}

	public static void setQuestionTestPath(String questionTestPath) {
		Configuration.questionTestPath = questionTestPath;
	}

	public static String getSmoOption() {
		return smoOption;
	}

	public static void setSmoOption(String smoOption) {
		Configuration.smoOption = smoOption;
	}

	public static String getPronounListPath() {
		return pronounListPath;
	}

	public static void setPronounListPath(String pronounListPath) {
		Configuration.pronounListPath = pronounListPath;
	}
}
