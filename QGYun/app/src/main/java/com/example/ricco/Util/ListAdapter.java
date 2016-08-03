package com.example.ricco.util;

import android.content.Context;
import android.widget.Filter;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by zydx on 2016/8/3.
 */
public class ListAdapter extends SimpleAdapter {

    private List<Map<String, Object>> dataList; //包含全部内容的数据源
    private List<Map<String, Object>> fdataList;   //过滤后的数据源
    private List<Map<String, Object>> mdataList;    //设置给Adapter的数据源
    private ListFilter listFilter;

    public ListAdapter(Context context, List<Map<String, Object>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        dataList = data;
    }

    @Override
    public Filter getFilter() {
        if (listFilter == null) {
            listFilter = new ListFilter();
        }

        return listFilter;
    }

    /**
     *  过滤器
     */
    class ListFilter extends Filter{
        //过滤内容
        @Override
        protected FilterResults performFiltering(CharSequence s) {
            FilterResults filterResults = new FilterResults();
            if(s.length() > 0){
                fdataList.clear();
                for (Map<String,Object> map:dataList) {
                    if(map.get("ResourceName").equals(s)){
                        fdataList.add(map);
                    }
                }
                filterResults.values = fdataList;
            }else {
                filterResults.values = dataList;
            }
            return filterResults;
        }
        //结果设置
        @Override
        protected void publishResults(CharSequence s, FilterResults filterResults) {
            mdataList = (List<Map<String, Object>>)filterResults.values;
            notifyDataSetChanged();
        }
    }
}
