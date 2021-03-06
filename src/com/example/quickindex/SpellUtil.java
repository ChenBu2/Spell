package com.example.quickindex;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import android.text.TextUtils;

/**
 * @author Mark C 汉字转拼音工具类(spell-->拼音)
 */
public class SpellUtil {
	public static String getSpell(String chinese) {
		if (TextUtils.isEmpty(chinese)) {
			return null;
		}
		// 用来设置转化的拼音的大小写，或者声调
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE);// 设置转化的拼音是大写字母
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 设置转化的拼音不带声调
		// 由于只能对单个汉字转化,所以需要将字符串转为字符数组,然后对每个字符转化,最后拼接起来
		char[] charArray = chinese.toCharArray();
		String spell = "";
		for (int i = 0; i < charArray.length; i++) {
			// 过滤空格 杰 克 ->JIEKE
			if (Character.isWhitespace(charArray[i])) {
				continue;
			}
			// 需要判断是否是汉字 , . @ # &
			// 汉字占2个字符,一个字节-128-->127,那么汉字肯定大于127
			if (charArray[i] > 127) {
				// 可能是汉字
				try {
					// 由于多音字的出现 如: 单 dan shan
					String[] spellArr = PinyinHelper.toHanyuPinyinStringArray(
							charArray[i], format);
					if (spellArr != null) {
						spell += spellArr[0]; //此处即使有多音字，那么也只能取第一个拼音
					} else {
						// 说明没有找到拼音,汉字有问题或不是汉字,可忽略
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
					//说明转化失败，不是汉字，比如O(∩_∩)O~，那么则忽略
				}
			} else {
				// 肯定不是汉字，应该是键盘上能够直接输入的字符，这些字符能够排序，但不能获取拼音
				// 所以可以直接拼接 a杰克->aheima
				spell += charArray[i];
			}
		}
		return spell;
	}
}
