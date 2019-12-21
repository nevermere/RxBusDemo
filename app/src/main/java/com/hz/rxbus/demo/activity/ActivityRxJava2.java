package com.hz.rxbus.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;

import com.hz.rxbus.demo.bean.FileBean;
import com.hz.rxbus.demo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.SchedulerSupport;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 描述：
 *
 * @author fzJiang
 * @date 2019-12-16 16:13.
 */
public class ActivityRxJava2 extends AppCompatActivity {

    public static final String TAG = "ActivityRxJava2";

    @BindView(R.id.btn_1)
    Button mBtn1;
    @BindView(R.id.btn_2)
    Button mBtn2;

    private Map<String, List<FileBean>> mFileMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava2);
        ButterKnife.bind(this);

        List<FileBean> list1 = new ArrayList<>();
        list1.add(new FileBean("1", "1_1"));
        list1.add(new FileBean("1", "1_2"));
        list1.add(new FileBean("1", "1_3"));
        mFileMap.put("1", list1);

        List<FileBean> list2 = new ArrayList<>();
        list2.add(new FileBean("2", "2_1"));
        list2.add(new FileBean("2", "2_2"));
        list2.add(new FileBean("2", "3_3"));
        mFileMap.put("2", list2);

        List<FileBean> list3 = new ArrayList<>();
        list3.add(new FileBean("3", "3_1"));
        list3.add(new FileBean("3", "3_2"));
        list3.add(new FileBean("3", "3_3"));
        mFileMap.put("3", list3);

        Log.i(TAG, "-------------Map初始化------------");

        for (Map.Entry<String, List<FileBean>> entry : mFileMap.entrySet()) {
            List<FileBean> list = entry.getValue();
            for (FileBean fileBean : list) {
                Log.i(TAG, fileBean.toString());
            }
        }
    }


    @OnClick({R.id.btn_1, R.id.btn_2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                compressLocalMediaMap(this, mFileMap, new Consumer<Map<String, List<FileBean>>>() {

                    @Override
                    public void accept(Map<String, List<FileBean>> stringListMap) throws Exception {
                        Log.i(TAG, "-----------Map压缩成功-------------:" + stringListMap.size());

                        for (Map.Entry<String, List<FileBean>> entry : stringListMap.entrySet()) {
                            List<FileBean> list = entry.getValue();
                            for (FileBean fileBean : list) {
                                Log.i(TAG, fileBean.toString());
                            }
                        }
                    }
                }, new Consumer<Throwable>() {

                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i(TAG, "Map压缩失败:" + throwable.getLocalizedMessage());
                    }
                });
                break;

            case R.id.btn_2:
                List<FileBean> list1 = new ArrayList<>();
                list1.add(new FileBean("1", "1_1"));
                list1.add(new FileBean("2", "1_2"));
                list1.add(new FileBean("3", "1_3"));


                compressFile(this, list1, new Consumer<List<FileBean>>() {

                    @Override
                    public void accept(List<FileBean> fileBeans) throws Exception {
                        Log.i(TAG, "List压缩成功:" + fileBeans.size());

                        for (FileBean fileBean : fileBeans) {
                            Log.i(TAG, fileBean.toString());
                        }
                    }
                }, new Consumer<Throwable>() {

                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i(TAG, "List压缩失败:" + throwable.getLocalizedMessage());
                    }
                });
                break;

            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public void compressLocalMediaMap(final Activity activity,
                                      final Map<String, List<FileBean>> fileMap,
                                      final Consumer<Map<String, List<FileBean>>> success,
                                      final Consumer<Throwable> error) {

        final String[] key = new String[1];

        Observable.just(fileMap)
                .map(new Function<Map<String, List<FileBean>>, Set<Map.Entry<String, List<FileBean>>>>() {

                    @Override
                    public Set<Map.Entry<String, List<FileBean>>> apply(Map<String, List<FileBean>> map) throws Exception {
                        // 开始遍历Map
                        Log.i(TAG, "------开始遍历Map------");
                        return map.entrySet();
                    }
                })
                .flatMap(new Function<Set<Map.Entry<String, List<FileBean>>>, ObservableSource<Map.Entry<String, List<FileBean>>>>() {

                    @Override
                    public ObservableSource<Map.Entry<String, List<FileBean>>> apply(Set<Map.Entry<String, List<FileBean>>> entries) throws Exception {
                        // 循环取出各List列表
                        Log.i(TAG, "------循环取出各List列表------");
                        return Observable.fromIterable(entries);
                    }
                })// .flatMapIterable(entries -> entries)
                .map(new Function<Map.Entry<String, List<FileBean>>, List<FileBean>>() {


                    @Override
                    public List<FileBean> apply(Map.Entry<String, List<FileBean>> entry) throws Exception {
                        // 开始处理List列表
                        key[0] = entry.getKey();
                        Log.i(TAG, "------开始处理List列表------" + key[0]);
                        return compressFile(activity, entry.getValue());
                    }
                })
//                .map(new Function<List<FileBean>, Map<String, List<FileBean>>>() {
//
//                    @Override
//                    public Map<String, List<FileBean>> apply(List<FileBean> list) throws Exception {
//                        // List处理完毕,依次放入Map集合中
//                        resultMap.put(key[0], list);
//                        Log.i(TAG, "------List处理完毕,依次放入Map集合中------" + key[0] + ",list:" + list.size() + ",resultMap:" + resultMap.size());
//                        return resultMap;
//                    }
//                })
                .collect(new Callable<Map<String, List<FileBean>>>() {

                    @Override
                    public Map<String, List<FileBean>> call() throws Exception {
                        return new HashMap<>(fileMap.size());
                    }
                }, new BiConsumer<Map<String, List<FileBean>>, List<FileBean>>() {

                    @Override
                    public void accept(Map<String, List<FileBean>> map, List<FileBean> list) throws Exception {
                        // List处理完毕,依次放入Map集合中
                        map.put(key[0], list);
                        Log.i(TAG, "------List处理完毕,依次放入Map集合中------" + key[0] + ",list:" + list.size() + ",map:" + map.size());
                    }
                })


                //.map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()))
                //.toMap(entry -> entry)

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success, error);
    }

    public static List<FileBean> compressFile(Activity activity, List<FileBean> fileList) {
        List<FileBean> resultList = new ArrayList<>();
        for (FileBean fileBean : fileList) {
            resultList.add(new FileBean(fileBean.getName() + "---", fileBean.getPath()));
        }
        return resultList;
    }

    @SuppressWarnings("unchecked")
    @CheckReturnValue
    @SchedulerSupport(SchedulerSupport.NONE)
    public static void compressFile(Activity activity,
                                    List<FileBean> fileList,
                                    Consumer<List<FileBean>> success,
                                    Consumer<Throwable> error) {

        SparseArray<FileBean> fileArray = new SparseArray<>();
        final int[] index = {0};

        Observable.fromArray(fileList)
                .flatMap(new Function<List<FileBean>, ObservableSource<FileBean>>() {

                    @Override
                    public ObservableSource<FileBean> apply(List<FileBean> list) throws Exception {
                        // 执行绑定
                        index[0] = 0;
                        Log.i(TAG, "执行绑定:" + index[0]);
                        return Observable.fromIterable(list);
                    }
                })
                .map(new Function<FileBean, String>() {

                    @Override
                    public String apply(FileBean fileBean) throws Exception {
                        // 数据处理
                        Log.i(TAG, "数据处理:" + (fileBean.getPath() + "_" + index[0]));
                        return fileBean.getPath() + "_" + index[0];
                    }
                })
                .map(new Function<String, FileBean>() {

                    @Override
                    public FileBean apply(String name) throws Exception {
                        // 执行压缩命令,并按顺序放入集合中
                        final FileBean file = new FileBean(String.valueOf(index[0]), name);
                        fileArray.put(index[0], file);

                        Log.i(TAG, "执行压缩命令,并按顺序放入集合中:" + index[0] + "--" + name);
                        return file;
                    }
                })
                .map(new Function<FileBean, FileBean>() {

                    @Override
                    public FileBean apply(FileBean fileBean) throws Exception {
                        // 按顺序取出
                        final FileBean fileOut = fileArray.get(index[0]);
                        index[0]++;

                        Log.i(TAG, "按顺序取出:" + index[0]);
                        return fileOut;
                    }
                })
                .collect(new Callable<List<FileBean>>() {

                    @Override
                    public List<FileBean> call() throws Exception {
                        Log.i(TAG, "数据收集,初始化");
                        return new ArrayList<>();
                    }
                }, new BiConsumer<List<FileBean>, FileBean>() {

                    @Override
                    public void accept(List<FileBean> fileListOut, FileBean fileOut) throws Exception {
                        Log.i(TAG, "数据收集,成功:" + fileOut.toString());
                        fileListOut.add(fileOut);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success, error);
    }
}