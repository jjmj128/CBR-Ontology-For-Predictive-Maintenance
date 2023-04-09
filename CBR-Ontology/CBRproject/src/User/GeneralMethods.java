package User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * This class contains some methods for data manipulation taht are used by other
 * pieces of code within the project.
 * 
 * @author Juan
 *
 */
public class GeneralMethods {

	/**
	 * 
	 * This method calculates the ontological similarity between two list of
	 * elements by considering which elements exist in both lists (intersection) and
	 * the elements which are different form one list to the other. A logarithmic
	 * normalization is performed to obtain a similarity value from 0 to 1. This
	 * method was proposed on the cited article:David Sánchez , Montserrat Batet,
	 * David Isern, Aida Valls. Ontology-based semantic similarity: A new
	 * feature-based approach. Expert Systems with Applications, ELSEVIER, 2012.
	 * 
	 * @param List1 containing on set of elements.
	 * @param List2 containing on set of elements.
	 * @return double similarity value for the pair of lists.
	 */
	public static double SimilarityValue(ArrayList<String> List1, ArrayList<String> List2) {

		double similarity_value = 0.0d;

		double FirstToSecond = 0.0d;// This variable will save the number of elements from the first list that are
									// not included in the second list.
		// It is an integer number even if the variable is declared as double to avoid
		// calculus problems.
		double SecondToFirst = 0.0d;// This variable will save the number of elements from the second list that are
									// not included in the first list.
		// It is an integer number even if the variable is declared as double to avoid
		// calculus problems.
		double Intersection = 0.0d;// This variable will save the number of elements that both lists have in
									// common.
		// It is an integer number even if the variable is declared as double to avoid
		// calculus problems.
		for (String s : List1) {// Iterates all elements in List1
			if (List2.contains(s)) {
				Intersection = Intersection + 1.0d;
				// The method Recommender.contains_similar is used so as to compare the elements
				// with the Levenshtein method.
				// This allows to avoid missing elements if misspelled.
			} else {

				FirstToSecond = FirstToSecond + 1.0d;// If they do not exist in the List2, they are part of the
														// difference between both lists.
			}

		}
		for (String s : List2) {
			if (List1.contains(s) == false) {
				SecondToFirst = SecondToFirst + 1.0d;// If they do not exist in the List1, they are part of the
														// difference between both lists.
				// The method Recommender.contains_similar is used so as to compare the elements
				// with the Levenshtein method.
				// This allows to avoid missing elements if misspelled.
			}

		}

		similarity_value = 1.0d
				- Math.log(1.0d + (FirstToSecond + SecondToFirst) / (FirstToSecond + SecondToFirst + Intersection))
						/ Math.log(2.0d);
		// The expression above calculates the normalized similarity.
		// The following logarithmic property concerning the logb(x) (logarithm of x in
		// base b) must be taken into account: logb(x)=ln(x)/ln(b).
		return similarity_value;
	}

	/**
	 * This method takes an String in which elements are separated by ', ' and
	 * returns an ArrayList containing each of those elements individually.
	 * 
	 * @param original_string in which some elements are separated by ', '
	 * @return ArrayList containing the String elements individually
	 */
	public static ArrayList<String> separate(String original_string) {
		List<String> items = Arrays.asList(original_string.split("\\s*,\\s*"));
		ArrayList<String> items_array = new ArrayList<String>(items);
		return items_array;
	}

	/**
	 * 
	 * 
	 * @param <T>         type of the elements of the Set provided
	 * @param originalSet is a Set of elements
	 * @return an ArrayList containing the elements in the originalSet
	 */
	public static <T> ArrayList<T> SetToArray(Set<T> originalSet) {
		ArrayList<T> newList = new ArrayList<T>();
		for (T obj : originalSet) {

			newList.add(obj);

		}
		return newList;
	}

	/**
	 * 
	 * @param <T>  type of the elements in the ArrayList provided
	 * @param list
	 * @return the list after deleting the repeated elements
	 */
	public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {

		// Create a new ArrayList
		ArrayList<T> newList = new ArrayList<T>();

		// Traverse through the first list
		for (T element : list) {

			// If this element is not present in newList
			// then add it
			if (!newList.contains(element)) {

				newList.add(element);
			}
		}

		// return the new list
		return newList;
	}

	public static List<Integer> StringListoIntList(List<String> original_list) {
		ArrayList<Integer> new_list = new ArrayList<Integer>();
		for (String s : original_list) {
			new_list.add(Integer.parseInt(s));

		}

		return new_list;
	}

}
