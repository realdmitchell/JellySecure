/*
 * Copyright (C) 2017 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lineageos.jellysecure.history;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.lineageos.jellysecure.MainActivity;
import org.lineageos.jellysecure.R;
import org.lineageos.jellysecure.utils.UiUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class HistoryHolder extends RecyclerView.ViewHolder {

    private final LinearLayout mRootLayout;
    private final TextView mTitle;
    private final TextView mSummary;

    HistoryHolder(View view) {
        super(view);
        mRootLayout = (LinearLayout) view.findViewById(R.id.row_history_layout);
        mTitle = (TextView) view.findViewById(R.id.row_history_title);
        mSummary = (TextView) view.findViewById(R.id.row_history_summary);
    }

    void bind(final Context context, final long id, String title, String url, long timestamp) {
        if (title == null || title.isEmpty()) {
            title = url.split("/")[2];
        }
        mTitle.setText(title);
        mSummary.setText(new SimpleDateFormat(context.getString(R.string.history_date_format),
                Locale.getDefault()).format(new Date(timestamp)));

        mRootLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        });

        mRootLayout.setOnLongClickListener(v -> {
            Uri uri = ContentUris.withAppendedId(HistoryProvider.Columns.CONTENT_URI, id);
            context.getContentResolver().delete(uri, null, null);
            return true;
        });

        int background;
        switch (UiUtils.getPositionInTime(timestamp)) {
            case 0:
                background = R.color.history_last_hour;
                break;
            case 1:
                background = R.color.history_today;
                break;
            case 2:
                background = R.color.history_this_week;
                break;
            case 3:
                background = R.color.history_this_month;
                break;
            default:
                background = R.color.history_earlier;
                break;
        }
        mRootLayout.setBackground(new ColorDrawable(ContextCompat.getColor(context, background)));
    }

}
