package org.easydarwin.easyplayer.main.fragments.filerecord;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.easydarwin.easyplayer.ProVideoActivity;
import org.easydarwin.easyplayer.R;
import org.easydarwin.easyplayer.databinding.FragmentMediaFileBinding;
import org.easydarwin.easyplayer.databinding.ImagePickerItemBinding;
import org.easydarwin.easyplayer.util.FileUtil;

import java.io.File;
import java.io.FilenameFilter;

public class LocalFileFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    public static final String KEY_IS_RECORD = "key_last_selection";

    private boolean mShowMp4File;
    private FragmentMediaFileBinding mBinding;

    SparseArray<Boolean> mImageChecked;

    private String mSuffix;
    File mRoot = null;
    File[] mSubFiles;
    int mImgHeight;
    public static LocalFileFragment newInstance(boolean isVideo) {
        Bundle args = new Bundle();
        args.putBoolean(KEY_IS_RECORD, isVideo);
        LocalFileFragment fragment = new LocalFileFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);

        mImageChecked = new SparseArray<>();

        mShowMp4File = getArguments().getBoolean(KEY_IS_RECORD);
        mSuffix = mShowMp4File ? ".mp4" : ".jpg";

        if (mShowMp4File) {
            mRoot = new File(FileUtil.getMoviePath());
        } else {
            mRoot = new File(FileUtil.getPicturePath());
        }

        File[] subFiles = mRoot.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.endsWith(mSuffix);
            }
        });

        if (subFiles == null)
            subFiles = new File[0];

        mSubFiles = subFiles;
        mImgHeight = (int) (getResources().getDisplayMetrics().density * 100 + 0.5f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_media_file, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mBinding.recycler.setLayoutManager(layoutManager);

        mBinding.recycler.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                ImagePickerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.image_picker_item, parent, false);
                return new ImageItemHolder(binding);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
                ImageItemHolder holder = (ImageItemHolder) viewHolder;
                holder.mCheckBox.setOnCheckedChangeListener(null);
                holder.mCheckBox.setChecked(mImageChecked.get(position, false));
                holder.mCheckBox.setOnCheckedChangeListener(LocalFileFragment.this);
                holder.mCheckBox.setTag(R.id.click_tag, holder);
                holder.mImage.setTag(R.id.click_tag, holder);

                if (mShowMp4File) {
                    holder.mPlayImage.setVisibility(View.VISIBLE);
                } else {
                    holder.mPlayImage.setVisibility(View.GONE);
                }

                Glide.with(getContext()).load(mSubFiles[position]).into(holder.mImage);
            }

            @Override
            public int getItemCount() {
                return mSubFiles.length;
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        ImageItemHolder holder = (ImageItemHolder) buttonView.getTag(R.id.click_tag);
//        int position = holder.getAdapterPosition();
    }

    @Override
    public void onClick(View v) {
        ImageItemHolder holder = (ImageItemHolder) v.getTag(R.id.click_tag);
        if (holder.getAdapterPosition() == RecyclerView.NO_POSITION) {
            return;
        }

        final String path = mSubFiles[holder.getAdapterPosition()].getPath();
        if (TextUtils.isEmpty(path)) {
            Toast.makeText(getContext(), "文件不存在", Toast.LENGTH_SHORT).show();
            return;
        }

        File f = new File(path);
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= 24) {//7.0 Android N
            //com.xxx.xxx.fileprovider为上述manifest中provider所配置相同
            uri = FileProvider.getUriForFile(getContext(), "org.easydarwin.easyplayer.fileProvider", f);
            // 读取权限，安装完毕以后，系统会自动收回权限，该过程没有用户交互
        } else {//7.0以下
            uri = Uri.fromFile(f);
        }
        if (path.endsWith(".jpg")) {
            try {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "image/*");
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        } else if (path.endsWith(".mp4")) {
            try {
                Intent i = new Intent(getContext(), ProVideoActivity.class);
                i.putExtra("videoPath", path);
                startActivity(i);

//                Intent intent = new Intent();
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                intent.setAction(Intent.ACTION_VIEW);
//                intent.setDataAndType(uri, "video/*");
//                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    class ImageItemHolder extends RecyclerView.ViewHolder {
        public final CheckBox mCheckBox;
        public final ImageView mImage;
        public final ImageView mPlayImage;

        public ImageItemHolder(ImagePickerItemBinding binding) {
            super(binding.getRoot());

            mCheckBox = binding.imageCheckbox;
            mImage = binding.imageIcon;
            mPlayImage = binding.imagePlay;
            mImage.setOnClickListener(LocalFileFragment.this);
        }
    }
}