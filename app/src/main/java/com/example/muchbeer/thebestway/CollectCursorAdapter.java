package com.example.muchbeer.thebestway;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by muchbeer on 3/13/2017.
 */

public class CollectCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link CollectCursorAdapter CursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public CollectCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvNameRegional = (TextView) view.findViewById(R.id.regional_name);
        TextView tvDirectional = (TextView) view.findViewById(R.id.edit_directional);
        TextView tvBasicNeed = (TextView) view.findViewById(R.id.basic_needs);
        TextView tvStatusDelete = (TextView) view.findViewById(R.id.status_delete);



        String nameRegional = cursor.getString(cursor.getColumnIndexOrThrow(CollectContract.RegionalEntry.COLUMN_REGIONAL));
        String Directional = cursor.getString(cursor.getColumnIndexOrThrow(CollectContract.RegionalEntry.COLUMN_DIRECTIONAL));
        String nameBasicNeed = cursor.getString(cursor.getColumnIndexOrThrow(CollectContract.RegionalEntry.COLUMN_REGIONAL_SETTING));
        String statusDelete = cursor.getString(cursor.getColumnIndexOrThrow(CollectContract.RegionalEntry.COLUMN_DELETE_LOCAL_DATA));


      /*  if (TextUtils.isEmpty(breed)) {
            breed = context.getString(R.string.unknown_breed);
        }*/

        tvNameRegional.setText(nameRegional);
        tvDirectional.setText(Directional);
        tvBasicNeed.setText(nameBasicNeed);
        tvStatusDelete.setText(statusDelete);
    }
}