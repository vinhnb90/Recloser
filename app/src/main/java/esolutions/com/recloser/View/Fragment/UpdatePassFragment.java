package esolutions.com.recloser.View.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import esolutions.com.recloser.Entity.ResponseServerLoginJSON;
import esolutions.com.recloser.R;
import esolutions.com.recloser.Utils.Class.Define;
import esolutions.com.recloser.Utils.DialogHelper.Entity.DialogEntity;

public class UpdatePassFragment extends BaseV4Fragment implements View.OnClickListener {
    private EditText mEtCurrentPass, mEtNewPass, mEtRetypeNewPass;
    private Button mBtnSave;

    private String mUser;

    private OnUpdatePassFragmentListener mListener;

    public UpdatePassFragment() {
        // Required empty public constructor
    }

    public static UpdatePassFragment newInstance(String user) throws Exception {
        if (user == null || user.isEmpty())
            throw new Exception("Lỗi khởi tạo Update info\nNội dung: user null!");

        UpdatePassFragment fragment = new UpdatePassFragment();
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
        View view = inflater.inflate(R.layout.fragment_update_pass, container, false);
        initViewV4Fragment(view);
        initSoureV4Fragment();
        setActionV4Fragment(savedInstanceState);
        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUpdatePassFragmentListener) {
            mListener = (OnUpdatePassFragmentListener) context;
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
        mEtCurrentPass = (EditText) view.findViewById(R.id.et_fragment_update_pass_current);
        mEtNewPass = (EditText) view.findViewById(R.id.et_fragment_update_pass_new);
        mEtRetypeNewPass = (EditText) view.findViewById(R.id.et_fragment_update_pass_new_retype);

        mBtnSave = (Button) view.findViewById(R.id.btn_fragment_update_pass_save);
    }

    @Override
    protected void initSoureV4Fragment() {

    }

    @Override
    protected void setActionV4Fragment(Bundle savedInstanceState) {
        mBtnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_fragment_update_pass_save:
                savePass();
                break;
        }
    }

    private void savePass() {
        boolean isNotFullPassField = false;
        String currentPass = mEtCurrentPass.getText().toString();
        String newPass = mEtNewPass.getText().toString();
        String retypeNewPass = mEtRetypeNewPass.getText().toString();
        if (currentPass.isEmpty()) {
            mEtCurrentPass.setError("Vui lòng nhập thông tin mật khẩu hiện tại.");
            isNotFullPassField = true;
        }

        if (newPass.isEmpty()) {
            mEtNewPass.setError("Vui lòng nhập thông tin mật khẩu mới.");
            isNotFullPassField = true;
        }

        if (retypeNewPass.isEmpty()) {
            mEtRetypeNewPass.setError("Vui lòng nhập thông tin mật khẩu mới một lần nữa.");
            isNotFullPassField = true;
        }

        if (!newPass.equals(retypeNewPass)) {
            mEtRetypeNewPass.setError("Mật khẩu nhập lại không đúng.");
            isNotFullPassField = true;
        }

        if (isNotFullPassField)
            return;

        mListener.updatePass(mUser, currentPass, newPass, retypeNewPass);
    }

    public void notifyResultUpdatePass(ResponseServerLoginJSON responseServerLoginJSON) {
        DialogEntity dialogEntity =
                (responseServerLoginJSON.getResult()) ?
                        new DialogEntity.DialogBuilder(getContext(), Define.STRING_DIALOG_HELPER.TITLE_DEFAULT.toString(), "Cập nhật mật khẩu thành công.").build()
                        :
                        new DialogEntity.DialogBuilder(getContext(), Define.STRING_DIALOG_HELPER.TITLE_DEFAULT.toString(), "Cập nhật thất bại.").build();

        mListener.showDialogUpdatePass(dialogEntity);
    }

    public interface OnUpdatePassFragmentListener {

        void updatePass(String user, String currentPass, String newPass, String retypeNewPass);

        void showDialogUpdatePass(DialogEntity dialogEntity);
    }
}
