package census_api_fetch;

public class Helper {
	
	public static String getLineOfCharacter(char character, int length) {
		String result = "";
		
		for ( int i = 0 ; i < length; i++ ) {
			result += character;
		}
		
		return result;
	}
}
