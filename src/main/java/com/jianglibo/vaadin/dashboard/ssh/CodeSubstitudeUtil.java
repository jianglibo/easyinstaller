package com.jianglibo.vaadin.dashboard.ssh;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.jianglibo.vaadin.dashboard.unused.StepRun;

/**
 * Only allow one level string and list of strings map. tags:
 * %%varname[].xx.xx%%, [%listvarnames%] [%/listvarname!%]
 * 
 * @author jianglibo@gmail.com
 *
 */
public class CodeSubstitudeUtil {

	protected static final Pattern VAR_PTN = Pattern.compile("\\]%([a-zA-Z]+(\\.[a-zA-Z]+|\\[\\d+\\])*?)+%\\[",
			Pattern.MULTILINE);
	protected static final Pattern ARRAY_VAR_PTN = Pattern.compile("^([a-zA-Z]+)(\\[\\d+\\])+$");
	protected static final Pattern ARRAY_ITEM_PTN = Pattern.compile("\\[(\\d+)\\]");

	public static String process(StepRun stepRun) {
		Map<String, Object> context = StepConfig.createStepConfig(stepRun).getMap();
		return process(stepRun.getCodeContent(), context);
	}
	public static String process(String content, Map<String, Object> context) {
		Matcher m = VAR_PTN.matcher(content);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			try {
				m.appendReplacement(sb, getValue(context, m.group(1)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		m.appendTail(sb);
		return sb.toString();
	}

	private static String getValue(Map<String, Object> context, String key) {
		String[] keys = key.split("\\.");
		Map<String, Object> tmpMap = context;
		int maxIdx = keys.length;
		for (int i = 0; i < maxIdx; i++) {
			boolean isLasti = maxIdx == (i + 1);
			String keyseg = keys[i];
			Matcher matcher = ARRAY_VAR_PTN.matcher(keyseg);
			boolean m = matcher.matches();
			List<Object> tmpList;
			if (m) { // is array like segment. a[0]
				tmpList = (List<Object>) tmpMap.get(matcher.group(1)); // is
																		// a
																		// list.
				Matcher arrayMatcher = ARRAY_ITEM_PTN.matcher(matcher.group(2));

				List<String> nested = Lists.newArrayList(); // a[1][2]

				while (arrayMatcher.find()) {
					nested.add(arrayMatcher.group(1));
				}

				int al = nested.size();

				for (int j = 0; j < al; j++) {
					boolean isLastj = j == (al - 1);
					if (isLasti) {
						if (isLastj) { // is the end of variable.
							return tmpList.get(Integer.valueOf(nested.get(j))).toString(); // for
																							// a[1][2],
																							// nested.get(j)
																							// ==
																							// 2
						} else {
							tmpList = (List<Object>) tmpList.get(Integer.valueOf(nested.get(j))); // still
																									// return
																									// list
						}
					} else { // not last variable. a[0][1].b
						if (isLastj) { // is the end of variable.
							tmpMap = (Map<String, Object>) tmpList.get(Integer.valueOf(nested.get(j)));
						} else {
							tmpList = (List<Object>) tmpList.get(Integer.valueOf(nested.get(j))); // still
						}
					}
				}
			} else {
				if (isLasti) {
					return tmpMap.get(keyseg).toString();
				} else {
					tmpMap = (Map<String, Object>) tmpMap.get(keyseg);
				}
			}
		}

		return returnOrigin(key);
	}

	private static String returnOrigin(String key) {
		return "]%" + key + "%[";
	}
}
