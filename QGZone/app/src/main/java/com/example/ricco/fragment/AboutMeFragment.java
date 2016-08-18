package com.example.ricco.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ricco.adapter.AboutAdapter;
import com.example.ricco.constant.Constant;
import com.example.ricco.entity.RelationModel;
import com.example.ricco.qgzone.MainActivity;
import com.example.ricco.qgzone.R;
import com.example.ricco.utils.HttpUtil;
import com.example.ricco.utils.JsonUtil;
import com.example.ricco.utils.SelectPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * 与我相关
 */
public class AboutMeFragment extends BaseFragment {

    private ListView aboutList;
    private SwipeRefreshLayout srl;
    private int page = 1;
    private boolean flag = true;
    private ArrayList<RelationModel> data = new ArrayList<RelationModel>();
    private AboutAdapter aa;
    //自定义的弹出框类
    private SelectPopupWindow popupWindow;
    private int pos;

    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener(){

        public void onClick(View v) {
            popupWindow.dismiss();
            switch (v.getId()) {
                case R.id.button:
                    Map<String, String> jsonObjiect = new HashMap<>();
                    jsonObjiect.put("relatedId", data.get(pos).getRelatedId()+"");
                    jsonObjiect.put("relationType", data.get(pos).getRelationType());
                    HttpUtil.Get(Constant.Account.RelationGetDetails+"?jsonObject="+JsonUtil.toJson(jsonObjiect), callBackListener);
                    Toast.makeText(getActivity(), "haha", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.button2:
                    sendRelation(Constant.Account.RelationDelete+"?jsonObject={\"relationId\"=\""+ data.get(pos).getRelationId() +"\"}");
                    data.remove(pos);
                    break;
                default:
                    break;
            }
        }
    };
     private HttpUtil.CallBackListener callBackListener = new HttpUtil.CallBackListener() {
         @Override
         public void OnFinish(String result) {
             Message msg = new Message();

             try {
                 JSONObject dataJson = new JSONObject(result);
                 msg.what = Integer.valueOf(dataJson.getString("state"));
                 msg.obj = dataJson.getString("Object");
                 Log.e("OnFinish: object", msg.obj+"");
             } catch (JSONException e) {
                 e.printStackTrace();
             } finally {
                 mHandler.sendMessage(msg);
             }
         }

         @Override
         public void OnError(Exception e) {

         }
     };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aboutme, container, false);

        aboutList = (ListView) view.findViewById(R.id.list_about_me);
        aa = new AboutAdapter(getActivity(), data);
        aboutList.setAdapter(aa);


        aboutList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (view.getLastVisiblePosition() == view.getCount() - 1 && flag) {
                        sendRelation(Constant.Account.RelationGet+"?jsonObject={\"page\"=\""+(++page)+"\"}");
                    } else if (!flag) {
                        Toast.makeText(getActivity(), "已显示全部记录", LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        aboutList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                //实例化SelectPicPopupWindow
                popupWindow = new SelectPopupWindow(getActivity(), itemsOnClick);
                popupWindow.initBtnName("查看动态详情", "删除动态");
                //显示窗口   设置layout在PopupWindow中显示的位置
                popupWindow.showAtLocation(getActivity().findViewById(R.id.about_fragment), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

        srl = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendRelation(Constant.Account.RelationRefresh);
            }
        });

        sendRelation(Constant.Account.RelationGet + "?jsonObject={\"page\"=\"" + page + "\"}");
        return view;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        MainActivity.nowFragTag = Constant.FRAGMENT_FLAG_ABOUTME;
    }

    // 发送注册信息给服务器
    private void sendRelation(final String url) {
        HttpUtil.Get(url, new HttpUtil.CallBackListener() {

            @Override
            public void OnFinish(String result) {

                Message msg = new Message();
                Log.e("OnFinish: result", result+"");

                AboutJsonModel object = JsonUtil.toObject(result, AboutJsonModel.class);
                msg.what = object.state;
                msg.obj = object.relations;

                mHandler.sendMessage(msg);
            }

            @Override
            public void OnError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 441:
                    data.addAll(0, (Collection<? extends RelationModel>) msg.obj);
                    aa.notifyDataSetChanged();
                    srl.setRefreshing(false);
                    break;
                case 411:
                    aa.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "删除成功", LENGTH_SHORT).show();
                    break;
                case 401:
                    if(msg.obj != null){
                        data.addAll((Collection<? extends RelationModel>) msg.obj);
                        aa.notifyDataSetChanged();
                    } else {
                        flag = false;
                    }
                    break;
                case 402:
                    Toast.makeText(getActivity(), "查看失败，请查看网络连接", LENGTH_SHORT).show();
                    break;
                case 442:
                    srl.setRefreshing(false);
                    Toast.makeText(getActivity(), "没有新消息", LENGTH_SHORT).show();
                    break;
                case 412:
                    Toast.makeText(getActivity(), "删除失败", LENGTH_SHORT).show();
                    break;
                case 403:
                    flag = false;
                    Toast.makeText(getActivity(), "已显示全部记录", LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    class AboutJsonModel {
        int state;
        List<RelationModel> relations;
    }

}