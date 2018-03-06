/**
 * Copyright (C) SAS Institute, All rights reserved.
 * General Public License: http://www.opensource.org/licenses/gpl-license.php
 */

/**
 * Logs for developers, not published to API DOC.
 *
 * History:
 * @date 2018-03-06    (Lei Wang) Initial release.
 */
package org.safs.rest.sample.util;

import java.text.DecimalFormat;

/**
 * @author Lei Wang
 *
 */
public class Util {
	public final static DecimalFormat decimalFormat2 = new DecimalFormat(".##");

	public static Double keep2Decimal(Double d){
		return Double.parseDouble(decimalFormat2.format(d));
	}
}
