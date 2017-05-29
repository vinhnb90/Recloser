package esolutions.com.recloser.Model;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import esolutions.com.recloser.Entity.HeaderNavigationMenuEntity;
import esolutions.com.recloser.R;

/**
 * Created by VinhNB on 2/17/2017.
 */

public class NavigationMenuApdater extends BaseExpandableListAdapter {
    private Context _context;
    private List<HeaderNavigationMenuEntity> menus;

    private HashMap<String, String> dataHeader;
    //dataChild = HashMap(KeyMenuLv1, HashMap<KeyMenuLv2, MenuLv2>)
    private HashMap<String, HashMap<String, String>> dataChild;
    private List<String> _listKeyHeader;
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listKeyChild;

    public NavigationMenuApdater(Context _context, List<HeaderNavigationMenuEntity> menus) {
        this._context = _context;

        if (menus == null)
            menus = new ArrayList<>();
        this.menus = menus;

        dataHeader = new HashMap<String, String>();
        dataChild = new HashMap<String, HashMap<String, String>>();
        _listKeyHeader = new ArrayList<>();
        _listKeyChild = new HashMap<String, List<String>>();

        for (HeaderNavigationMenuEntity menu :
                menus) {
            String keyMenuLv1 = menu.getKeyMenuLv1();
            dataHeader.put(keyMenuLv1, menu.getMenuLv1());
            _listKeyHeader.add(keyMenuLv1);
            dataChild.put(keyMenuLv1, menu.getMenuLv2());
            List<String> keyMenuLv2s = new ArrayList<>();
            for (String keyMenuLv2 :
                    menu.getMenuLv2().keySet()) {
                keyMenuLv2s.add(keyMenuLv2);
            }
            _listKeyChild.put(menu.getKeyMenuLv1(), keyMenuLv2s);
        }
    }

    @Override
    public int getGroupCount() {
        return this._listKeyHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String keyGroup = this._listKeyHeader.get(groupPosition);
        return this.dataChild.get(keyGroup).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        String keyGroup = getGroupKey(groupPosition);
        String group = dataHeader.get(keyGroup);
        return group;
    }

    public String getGroupKey(int groupPosition) {
        return this._listKeyHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String keyGroup = getGroupKey(groupPosition);
        String keyChild = getChildKey(groupPosition, childPosition);
        String child = dataChild.get(keyGroup).get(keyChild);
        return child;
    }

    public String getChildKey(int groupPosition, int childPosition) {
        return this._listKeyChild.get(getGroupKey(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.ac_main_row_navi_menu_header, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.ac_main_row_navi_menu_tv_header);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

         ImageView ivIconExpandCollapse = (ImageView) convertView.findViewById(R.id.ac_main_row_navi_menu_tv_header_iv_icon_expand_collapse);
        if (isExpanded) {
            ivIconExpandCollapse.setBackgroundResource(R.drawable.ic_arrow_expand);
        } else {
            ivIconExpandCollapse.setBackgroundResource(R.drawable.ic_arrow_collapse);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.ac_main_row_navi_menu, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.ac_main_row_navi_menu_tv);

        txtListChild.setText(childText);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
