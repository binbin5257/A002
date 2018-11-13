/*
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.lds.chatcore.view.widget.timepicker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.lds.chatcore.R;

/**
 * Numeric Wheel adapter.
 */
public class NumericMinWheelAdapter extends AbstractWheelTextAdapter {

	/**
	 * The default min value
	 */
	public static final int DEFAULT_MAX_VALUE = 9;

	/**
	 * The default max value
	 */
	private static final int DEFAULT_MIN_VALUE = 0;

	/**
	 * The default max value
	 */
	private static final int DEFAULT_INTERVAL = 0;

	// Values
	private int minValue;
	private int maxValue;
	private int select;
	private int interval;

	// format
	private String format;

	/**
	 * Constructor
	 *
	 * @param context the current context
	 */
	public NumericMinWheelAdapter(Context context) {
		this(context, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE, DEFAULT_INTERVAL, 0);
	}

	/**
	 * Constructor
	 *
	 * @param context  the current context
	 * @param minValue the wheel min value
	 * @param maxValue the wheel max value
	 */
	public NumericMinWheelAdapter(Context context, int minValue, int maxValue, int interval, int select) {
		this(context, minValue, maxValue, interval, null, select);
	}

	/**
	 * Constructor
	 *
	 * @param context  the current context
	 * @param minValue the wheel min value
	 * @param maxValue the wheel max value
	 * @param format   the format string
	 */
	public NumericMinWheelAdapter(Context context, int minValue, int maxValue, int interval, String format, int select) {
		super(context, R.layout.wheel_text_item, NO_RESOURCE);

		this.minValue = minValue;
		this.maxValue = maxValue;
		this.select = select;
		this.format = format;
		this.interval = interval;
		setItemTextResource(R.id.time_text);
	}

	@Override
	public CharSequence getItemText(int index) {
		if (index >= 0 && index < getItemsCount()) {
			int value = minValue + (index * interval);
			return format != null ? String.format(format, value) : Integer.toString(value);
		}
		return null;
	}

	@Override
	public int getItemsCount() {
		if (interval == 0)
			return maxValue - minValue + 1;
		return maxValue / interval - minValue + 1;
	}

	@Override
	public View getItem(int index, View convertView, ViewGroup parent) {

		View view = super.getItem(index, convertView, parent);

		TextView h = (TextView) view
				.findViewById(R.id.time_text);
		if (index == select) {
			h.setTextColor(0xFF5d5d5d);
		} else {
			h.setTextColor(0xFFe5e5e5);
		}
		h.setText(getItemText(index));

		return view;
	}
}
