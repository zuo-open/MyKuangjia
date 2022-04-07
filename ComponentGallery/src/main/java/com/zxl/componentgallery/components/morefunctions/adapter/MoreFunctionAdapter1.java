package com.zxl.componentgallery.components.morefunctions.adapter;

import static com.zxl.componentgallery.components.morefunctions.bean.FunctionBeanKt.TAG_FUNCTION;
import static com.zxl.componentgallery.components.morefunctions.bean.FunctionBeanKt.TAG_SUBTITLE;
import static com.zxl.componentgallery.components.morefunctions.bean.FunctionBeanKt.TAG_TITLE;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseProviderMultiAdapter;
import com.zxl.componentgallery.R;
import com.zxl.componentgallery.components.morefunctions.bean.FunctionBean;

import java.util.List;

public class MoreFunctionAdapter1 extends BaseProviderMultiAdapter<FunctionBean> {
    public MoreFunctionAdapter1() {
        super();
        addItemProvider(new FunctionAdapter(TAG_FUNCTION, R.layout.item_function));
        addItemProvider(new TitleAdapter(TAG_TITLE, R.layout.item_title));
        addItemProvider(new SubTitleAdapter(TAG_SUBTITLE, R.layout.item_subtitle));
    }

    @Override
    protected int getItemType(@NonNull List<? extends FunctionBean> list, int i) {
        FunctionBean functionBean = list.get(i);
        return functionBean.getType();
    }
}
