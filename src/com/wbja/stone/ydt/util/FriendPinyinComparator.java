package com.wbja.stone.ydt.util;

import java.util.Comparator;

import com.wbja.stone.ydt.entity.Friend;

/**
 * 
 * @author xiaanming
 *
 */
public class FriendPinyinComparator implements Comparator<Friend> {

	public int compare(Friend o1, Friend o2) {
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
