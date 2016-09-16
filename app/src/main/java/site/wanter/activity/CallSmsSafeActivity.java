package site.wanter.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import site.wanter.bean.BlackNumInfo;
import site.wanter.db.dao.BlackNumDao;
import site.wanter.mobilesafe.R;
import site.wanter.utils.MyAsycnTask;

/**
 * Created by Huyanglin on 2016/9/1.
 */
public class CallSmsSafeActivity extends Activity {
    private Context mContext;
    private ListView lv_callsmssafe_blacknum;
    private ProgressBar loading;
    BlackNumDao blackNumDao;
    private List<BlackNumInfo> list;
    private MyAdapter myAdapter;
    private AlertDialog dialog;
    private final int MAXNUM=20;
    private int startindex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_callsmssafe);
        blackNumDao = new BlackNumDao(mContext);
        lv_callsmssafe_blacknum = (ListView) findViewById(R.id.lv_callsmssafe_blacknum);
        loading = (ProgressBar) findViewById(R.id.loading);
        fillData();
        lv_callsmssafe_blacknum.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //滑动状态改变调用
                if (scrollState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    //静止状态
                    int position = lv_callsmssafe_blacknum.getLastVisiblePosition();
                    if (position==list.size()-1){
                        //加载下一波数据
                        //更新查询位置
                        startindex+=20;
                        fillData();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //滑动的时候调用
            }
        });
    }

    /**
     * 异步加载数据
     */
    private void fillData() {
        new MyAsycnTask() {

            @Override
            public void preTask() {
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void doInTask() {
                if (list==null){
                    list = blackNumDao.getPartBlackNum(MAXNUM,startindex);
                }else {
                    list.addAll(blackNumDao.getPartBlackNum(MAXNUM,startindex));//保证向上滑动的时候可以正常显示之前的数据
                }
            }

            @Override
            public void postTask() {
                if (myAdapter == null) {
                    myAdapter = new MyAdapter();
                    lv_callsmssafe_blacknum.setAdapter(myAdapter);
                }else{
                    myAdapter.notifyDataSetChanged();
                }
                loading.setVisibility(View.INVISIBLE);
//                loading.setVisibility(View.INVISIBLE);
//                myAdapter=new MyAdapter();
//                lv_callsmssafe_blacknum.setAdapter(myAdapter);
            }
        }.execute();//注意必须执行
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final BlackNumInfo blackNumInfo=list.get(position);
            //listview复用缓存，防止OOM
            View view=null;
            ViewHolder viewHolder;
            if (convertView==null){
                view = View.inflate(mContext, R.layout.item_callsmssafe, null);
                viewHolder=new ViewHolder();
                viewHolder.iv_itemcallsmssafe_delete=(ImageView) view.findViewById(R.id.iv_itemcallsmssafe_delete);
                viewHolder.tv_itemcallsmssafe_blacknum = (TextView) view.findViewById(R.id.tv_itemcallsmssafe_blacknum);
                viewHolder.tv_itemcallsmssafe_mode = (TextView) view.findViewById(R.id.tv_itemcallsmssafe_mode);
                view.setTag(viewHolder);//将容器和对象一起绑定
            }else {
                view=convertView;
                viewHolder = (ViewHolder) view.getTag();
            }

            //设置显示数据
            viewHolder.tv_itemcallsmssafe_blacknum.setText(list.get(position).getBlacknum());
            switch (list.get(position).getMode()) {
                case BlackNumDao.CALL:
                    viewHolder.tv_itemcallsmssafe_mode.setText("电话拦截");
                    break;

                case BlackNumDao.SMS:
                    viewHolder.tv_itemcallsmssafe_mode.setText("短信拦截");
                    break;

                case BlackNumDao.ALL:
                    viewHolder.tv_itemcallsmssafe_mode.setText("全部拦截");
                    break;
            }

            viewHolder.iv_itemcallsmssafe_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
                    builder.setMessage("您确认要删除号码："+blackNumInfo.getBlacknum()+"吗?");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            blackNumDao.deleteBlackNum(blackNumInfo.getBlacknum());//从数据库删除黑名单号码
                            list.remove(position);//从存放黑名单的list集合中删除相应的数据
                            //重点！更新界面
                            myAdapter.notifyDataSetChanged();
                            dialog.dismiss();

                        }
                    });
                    builder.setNegativeButton("取消",null);
                    builder.show();
                }
            });

            return view;
        }
    }

    /**
     * viewholder实现复用
     */
    class ViewHolder{
        TextView tv_itemcallsmssafe_blacknum,tv_itemcallsmssafe_mode;
        ImageView iv_itemcallsmssafe_delete;
    }

    /**
     *添加黑名单号码
     */
    public void addblacknum(View v){
        AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext, R.layout.dialog_addblacknum, null);
        //初始化控件
        final EditText et_callsmssafe_addblacknum = (EditText) view.findViewById(R.id.et_callsmssafe_addblacknum);
        final RadioGroup rg_addblacknum_modes = (RadioGroup) view.findViewById(R.id.rg_addblacknum_modes);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        Button btn_cancle = (Button) view.findViewById(R.id.btn_cancle);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //确定按钮点击事件
                String blacknum = et_callsmssafe_addblacknum.getText().toString().trim();
                if (TextUtils.isEmpty(blacknum)){
                    Toast.makeText(mContext, "请输入黑名单号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                int mode=-1;
                int checkedRadioButtonId = rg_addblacknum_modes.getCheckedRadioButtonId();
                switch(checkedRadioButtonId){
                    case R.id.rb_addblacknum_tel :
                        //电话拦截
                        mode=BlackNumDao.CALL;
                        break;

                    case R.id.rb_addblacknum_sms :
                        //短信拦截
                        mode=BlackNumDao.SMS;

                        break;

                    case R.id.rb_addblacknum_all :
                        //全部拦截
                        mode=BlackNumDao.ALL;

                        break;
                }
                blackNumDao.addBlackNum(blacknum,mode);
                //添加到list集合中，并且更新界面
                list.add(0,new BlackNumInfo(blacknum,mode));
                myAdapter.notifyDataSetChanged();
                dialog.dismiss();

            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消按钮点击事件
                dialog.dismiss();
            }
        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }
}