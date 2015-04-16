public class TweetingRule {
	private String who;
	private String[] ConstainsThis;
	private String[] ConstainsThat;
	private String[] notThis;
	private String[] thenTweet;

	public TweetingRule(String _who, String _containsThis,
			String _containsThat, String _thenTweet, String _notThis,
			String _separator) {
		ConstainsThis = _containsThis.split(_separator);
		ConstainsThat = _containsThat.split(_separator);
		notThis = _notThis.split(_separator);
		thenTweet = _thenTweet.split(_separator);
	}

	public String toString() {
		String result = "";
		result += " ContainsThis: " + ConstainsThis.toString();
		return result;

	}

	public boolean checkIfMatch(String _newTweet) {
		boolean result = false;
		String newTweet = _newTweet;
		newTweet = newTweet.toLowerCase();
		boolean foundFirst = false;
		for (String word : ConstainsThis) {
			if (newTweet.contains(word.toLowerCase())) {
				foundFirst = true;
				break;
			}
		}
		if (foundFirst) {
			for (String word : ConstainsThat) {
				if (newTweet.contains(word.toLowerCase())) {
					result = true;
					break;
				}
			}
		}
		if (foundFirst) {
			for (String word : notThis) {
				if (word.length() > 1) {
					if (newTweet.contains(word.toLowerCase())) {
						result = false;
						break;
					}
				}
			}
		}
		return result;
	}

	/*
	 * FIXME: add the ability to make sure it always returns all of the
	 * thenTweet in a random manner ie not one twice and another never...
	 */
	public String getRandomTweet() {
		return thenTweet[(int) (Math.random() * thenTweet.length)];
	}

}
