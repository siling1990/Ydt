package com.wbja.stone.ydt.util;

import java.util.Comparator;

import com.wbja.stone.ydt.entity.Patient;

/**
 * 
 * @author xiaanming
 *
 */
public class PatientPinyinComparator implements Comparator<Patient> {

	public int compare(Patient o1, Patient o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
