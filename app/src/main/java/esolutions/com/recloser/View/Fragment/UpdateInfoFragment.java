package esolutions.com.recloser.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import esolutions.com.recloser.Entity.InfoEntity;
import esolutions.com.recloser.Entity.ResponseServerLoginJSON;
import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.Define;
import esolutions.com.recloser.Utils.DialogHelper.Entity.DialogEntity;

public class UpdateInfoFragment extends BaseV4Fragment implements View.OnClickListener {
    private EditText mEtName, mEtPhone, mEtEmail;
    private Button mBtnSave;

    private String mUser;

    private OnUpdateInfoFragmentListener mListener;

    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    public static UpdateInfoFragment newInstance(String user) throws Exception {
        if (user == null || user.isEmpty())
            throw new Exception("Lỗi khởi tạo Update info\nNội dung: user null!");

        UpdateInfoFragment fragment = new UpdateInfoFragment();
        Bundle args = new Bundle();
        args.putString(Define.PARAM_NAME_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUser = getArguments().getString(Define.PARAM_NAME_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_info, container, false);
        initViewV4Fragment(view);
        initSoureV4Fragment();
        setActionV4Fragment(savedInstanceState);
        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUpdateInfoFragmentListener) {
            mListener = (OnUpdateInfoFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUpdatePassFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected void initViewV4Fragment(View view) {
        mEtName = (EditText) view.findViewById(R.id.et_fragment_update_info_name);
        mEtPhone = (EditText) view.findViewById(R.id.et_fragment_update_info_phone);
        mEtEmail = (EditText) view.findViewById(R.id.et_fragment_update_info_email);

        mBtnSave = (Button) view.findViewById(R.id.btn_fragment_update_info_save);
    }

    @Override
    protected void initSoureV4Fragment() {

    }

    @Override
    protected void setActionV4Fragment(Bundle savedInstanceState) {
        mBtnSave.setOnClickListener(this);

        mListener.GetInfo(mUser);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_fragment_update_info_save:
                saveInfo();
                break;
        }
    }

    private void saveInfo() {
        boolean isNotFullInfo = false;
        String fullName = mEtName.getText().toString();
        String phone = mEtPhone.getText().toString();
        String email = mEtEmail.getText().toString();
        if (fullName.isEmpty()) {
            mEtName.setError("Vui lòng nhập tên.");
            isNotFullInfo = true;
        }

        if (phone.isEmpty()) {
            mEtPhone.setError("Vui lòng nhập số điện thoại.");
            isNotFullInfo = true;
        }

        if (email.isEmpty()) {
            mEtEmail.setError("Vui lòng nhập email.");
            isNotFullInfo = true;
        }

        if (isNotFullInfo)
            return;

        mListener.UpdateInfo(mUser, fullName, phone, email);
    }

    public void notifyResultUpdateInfo(ResponseServerLoginJSON responseServerLoginJSON) {
        DialogEntity dialogEntity =
                (responseServerLoginJSON.getResult()) ?
                        new DialogEntity.DialogBuilder(getContext(), Define.STRING_DIALOG_HELPER.TITLE_DEFAULT.toString(), "Cập nhật thành công.").build()
                        :
                        new DialogEntity.DialogBuilder(getContext(), Define.STRING_DIALOG_HELPER.TITLE_DEFAULT.toString(), "Cập nhật thất bại.").build();

        mListener.showDialogUpdateInfo(dialogEntity);
    }

    public void fillDataInfoUser(InfoEntity infoEntity) {
        if (infoEntity == null)
            return;
        mEtName.setText(infoEntity.getFullName());
        mEtPhone.setText(infoEntity.getPhoneNumber());
        mEtEmail.setText(infoEntity.getEmail());
    }

    public interface OnUpdateInfoFragmentListener {

        void GetInfo(String user);

        void UpdateInfo(String name, String fullName, String phone, String email);

        void showDialogUpdateInfo(DialogEntity dialogEntity);
    }
}
