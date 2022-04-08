package com.ashleymccallum.madimalcrossing.VillagerListRecycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ashleymccallum.madimalcrossing.R;
import com.ashleymccallum.madimalcrossing.pojos.VillagerList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FilterListViewAdapter extends ArrayAdapter<String> {

    private final List<String> selection;
    private boolean adding;

    public FilterListViewAdapter(@NonNull Context context, List<String> filters) {
        super(context, 0, filters);
        selection = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.add_listview_item, parent, false);
        }

        ImageView checkbox = convertView.findViewById(R.id.addToListCheck);

        if(selection.contains(getItem(position))) {
            checkbox.setImageResource(R.drawable.ic_outline_check_box_24);
        } else {
            checkbox.setImageResource(R.drawable.ic_outline_check_box_outline_blank_24);
        }

        TextView name = convertView.findViewById(R.id.addListName);
        name.setText(getItem(position));

        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adding = addRemove(checkbox, adding);
                if(adding) {
                    selection.add(getItem(position));
                } else {
                    selection.remove(getItem(position));
                }
            }
        });
        return convertView;
    }

    /**
     * Flags if an item is being added or removed from the selection
     * @param imageView the ImageView to update
     * @param adding a boolean flagging if the item is currently being added or not
     * @return an update boolean if the item is to be added or not
     * @author Ashley McCallum
     */
    private boolean addRemove(ImageView imageView, boolean adding) {
        if(adding) {
            adding = false;
            imageView.setImageResource(R.drawable.ic_outline_check_box_outline_blank_24);
        } else {
            adding = true;
            imageView.setImageResource(R.drawable.ic_outline_check_box_24);
        }
        return adding;
    }

    public List<String> getSelection() {
        return this.selection;
    }
}
