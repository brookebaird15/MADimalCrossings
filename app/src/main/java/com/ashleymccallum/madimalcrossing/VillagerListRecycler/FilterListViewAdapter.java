package com.ashleymccallum.madimalcrossing.VillagerListRecycler;

import android.content.Context;
import android.util.Log;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterListViewAdapter extends ArrayAdapter<String> {

    private final Set<String> selection;
    private boolean adding;

    public FilterListViewAdapter(@NonNull Context context, List<String> filters, Set<String> previousSelection) {
        super(context, 0, filters);

        if(previousSelection != null) {
            this.selection = new HashSet<>(previousSelection);
        } else {
            selection = new HashSet<>();
        }
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
            adding = true;
        } else {
            checkbox.setImageResource(R.drawable.ic_outline_check_box_outline_blank_24);
            adding = false;
        }

        TextView name = convertView.findViewById(R.id.addListName);
        name.setText(getItem(position));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adding = addRemove(checkbox, getItem(position));
            }
        });
        return convertView;
    }

    /**
     * Flags if an item is being added or removed from the selection
     * @param imageView the ImageView to update
     * @return an update boolean if the item is to be added or not
     * @author Ashley McCallum
     */
    private boolean addRemove(ImageView imageView, String item) {
        if(adding) {
            adding = false;
            imageView.setImageResource(R.drawable.ic_outline_check_box_outline_blank_24);
            selection.remove(item);
        } else {
            adding = true;
            imageView.setImageResource(R.drawable.ic_outline_check_box_24);
            selection.add(item);
        }
        return adding;
    }

    public Set<String> getSelection() {
        return this.selection;
    }
}
