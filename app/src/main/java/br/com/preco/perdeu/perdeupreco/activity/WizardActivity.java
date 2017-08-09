package br.com.preco.perdeu.perdeupreco.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import br.com.preco.perdeu.perdeupreco.R;
import br.com.preco.perdeu.perdeupreco.WizardQuestion;
import br.com.preco.perdeu.perdeupreco.bean.Usuario;
import br.com.preco.perdeu.perdeupreco.util.UtilWS;
import br.com.preco.perdeu.perdeupreco.wizard.model.AbstractWizardModel;
import br.com.preco.perdeu.perdeupreco.wizard.model.ModelCallbacks;
import br.com.preco.perdeu.perdeupreco.wizard.model.Page;
import br.com.preco.perdeu.perdeupreco.wizard.model.ReviewItem;
import br.com.preco.perdeu.perdeupreco.wizard.model.SingleFixedChoicePage;
import br.com.preco.perdeu.perdeupreco.wizard.ui.PageFragmentCallbacks;
import br.com.preco.perdeu.perdeupreco.wizard.ui.ReviewFragment;
import br.com.preco.perdeu.perdeupreco.wizard.ui.StepPagerStrip;

/**
 * Activity que deve mostrar o wizard no 1o acesso do usuário.
 * Created by Ronan Lima on 22/02/2016.
 */
public class WizardActivity extends ActionBarActivity implements PageFragmentCallbacks
        , ReviewFragment.Callbacks, ModelCallbacks {
    private static final String welComeWizard = "wizardQueryBr4devDisplayed";
    private static final String TAG = "WizardQuery";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private AbstractWizardModel wizardModel = new WizardQuestion(this);
    private MyPagerAdapter pagerAdapter;
    private List<Page> listaPaginasWizard;
    private ViewPager viewPager;
    private StepPagerStrip stepPage;
    public Toolbar toolbar;
    private Button prevButton, nextButton;
    private boolean consumePageSelectedEvent, editingAfterReview;
    private SharedPreferences myPrefs;
    private FloatingActionButton floatButton = null;
    private String emailManual = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean wizardDisplayed = myPrefs.getBoolean(welComeWizard, false);

        gerenciaChamadaWizard(savedInstanceState);// TODO ronan.lima: remover esta linha e descomentar codigo abaixo.
        /*if (!wizardDisplayed){
            gerenciaChamadaWizard(savedInstanceState);
        } else {
            Intent intent = new Intent(this, GpsActivity.class);
            startActivity(intent);
            finish();
        }*/

    }

    private void gerenciaChamadaWizard(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_instruction);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.drawable.ic_logo);
        setSupportActionBar(toolbar);

        wizardModel.registerListener(this);
        pagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);
        stepPage = (StepPagerStrip) findViewById(R.id.strip);
        stepPage.setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
            @Override
            public void onPageStripSelected(int position) {
                position = Math.min(pagerAdapter.getCount() - 1, position);
                if (position != viewPager.getCurrentItem()) {
                    viewPager.setCurrentItem(position);
                }
            }
        });

        prevButton = (Button) findViewById(R.id.prev_button);
        nextButton = (Button) findViewById(R.id.next_button);

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                stepPage.setCurrentPage(position);

                /*if(floatButton != null && position != 3){
                    floatButton.setVisibility(View.INVISIBLE);
                }*/

                if (consumePageSelectedEvent) {
                    consumePageSelectedEvent = false;
                    return;
                }
                editingAfterReview = false;
                updateBottomBar();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == listaPaginasWizard.size()) {
                    processaInformacoesUsuario();
                    addWizardInSharedPreferences();
                    IntentIntegrator integrator = new IntentIntegrator(WizardActivity.this);
                    integrator.setCaptureActivity(QrCodeActivity.class);
                    integrator.addExtra("SCAN_MODE", "QR_CODE_MODE");
                    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                    integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
                    //new IntentIntegrator(WizardActivity.this).setCaptureActivity(QrCodeActivity.class).initiateScan();
//                    Intent intent = new Intent(getBaseContext(), QrCodeActivity.class);
//                    startActivity(intent);
//                    finish();
                } else {
                    if (editingAfterReview) {
                        viewPager.setCurrentItem(pagerAdapter.getCount() - 1);
                    } else {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    }
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        });

        if (savedInstanceState != null) {

            wizardModel.load(savedInstanceState.getBundle("model"));

        } else {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                int hasWriteContactsPermission = ContextCompat.checkSelfPermission(WizardActivity.this,Manifest.permission.GET_ACCOUNTS);
                if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(WizardActivity.this,Manifest.permission.GET_ACCOUNTS)) {
                        showMessageOKCancel("Precisamos ter acesso aos seus emails",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ActivityCompat.requestPermissions(WizardActivity.this,new String[] {Manifest.permission.GET_ACCOUNTS},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                });
                        return;
                    }

                    ActivityCompat.requestPermissions(WizardActivity.this,new String[] {Manifest.permission.GET_ACCOUNTS},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    return;
                }

                wizardModel.getmRootPageList().add(createPageEmail(getAccounts(this), this.wizardModel).setRequired(true));

                onPageTreeChanged();
                updateBottomBar();

            }else{

                wizardModel.getmRootPageList().add(createPageEmail(getAccounts(this), this.wizardModel).setRequired(true));

                onPageTreeChanged();
                updateBottomBar();
            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if (result.getContents() == null){
                Log.d(TAG, "Nao leu porra nenhuma! Faz essa merda direito...");
            } else {
                Log.d(TAG, "Scanned.");
                Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addWizardInSharedPreferences(){
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putBoolean(welComeWizard, true);
        editor.commit();
    }

    private void processaInformacoesUsuario(){
        ArrayList<ReviewItem> reviewItems = new ArrayList<ReviewItem>();
        for (Page page : wizardModel.getCurrentPageSequence()) {
            page.getReviewItems(reviewItems);
        }
        Usuario user = new Usuario();
        user.setSexo(reviewItems.get(0).getmDisplayValue().substring(0, 1).toUpperCase());
        user.setEmail(reviewItems.get(2).getmDisplayValue());

        // Percorre enum para simular lista de faixa etaria que devemos buscar do webservice.
        for (FaixaEtaria item: FaixaEtaria.class.getEnumConstants()) {
            if (item.getLabel().equalsIgnoreCase(reviewItems.get(1).getmDisplayValue())){
                user.setFaixaEtaria(item.ordinal());
                break;
            }
        }
        UtilWS.callWsToInsertUser(user, TAG);
    }

    public enum FaixaEtaria{
//        "Menos de 18", "18 - 25", "26 - 40", "Acima de 40"
        RANGE_UM("Menos de 16"), RANGE_DOIS("16 - 20"), RANGE_TRES("21 - 30"), RANGE_QUATRO("31 - 40")
        , RANGE_CINCO("41 - 50"), RANGE_SEIS("Acima de 50");

        private String label;

        private FaixaEtaria(String label){
            setLabel(label);
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("model", wizardModel.save());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wizardModel.unregisterListener(this);
    }

    private void updateBottomBar() {
        int position = viewPager.getCurrentItem();
        if (position == listaPaginasWizard.size()){
            nextButton.setText(R.string.txt_botao_fim_questionario);
            nextButton.setBackgroundResource(R.drawable.finish_background);
        } else {
            nextButton.setText(editingAfterReview ? R.string.txt_botao_revisao_informacoes : R.string.txt_botao_proximo);
            nextButton.setBackgroundResource(R.drawable.selectable_item_background);
            TypedValue value = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, value, true);
            nextButton.setEnabled(position != pagerAdapter.getCutOffPage());
            /*if (position == listaPaginasWizard.size() - 1){
                habilitaFloatButton();
            }*/
        }
        prevButton.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void onPageDataChanged(Page page) {
        if (page.isRequired()){
            if (recalculateCutOffPage()) {
                pagerAdapter.notifyDataSetChanged();
                updateBottomBar();
            }
        }
    }

    private boolean recalculateCutOffPage() {
        int cutOffPage = listaPaginasWizard.size() + 1;
        for (int i = 0 ; i < listaPaginasWizard.size(); i++) {
            Page page = listaPaginasWizard.get(i);
            if (page.isRequired() && !page.isCompleted()){
                cutOffPage = i;
                break;
            }
        }

        if (pagerAdapter.getCutOffPage() != cutOffPage){
            pagerAdapter.setCutOffPage(cutOffPage);
            return true;
        }
        return false;
    }

    @Override
    public void onPageTreeChanged() {
        listaPaginasWizard = wizardModel.getCurrentPageSequence();
        recalculateCutOffPage();
        stepPage.setPageCount(listaPaginasWizard.size() + 1);
        pagerAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    @Override
    public Page onGetPage(String key) {
        return wizardModel.findByKey(key);
    }

    @Override
    public AbstractWizardModel onGetModel() {
        return wizardModel;
    }

    @Override
    public void onEditScreenAfterReview(String pageKey) {
        for (int i = listaPaginasWizard.size() -1; i >= 0; i--){
            if (listaPaginasWizard.get(i).getKey().equals(pageKey)){
                consumePageSelectedEvent = true;
                editingAfterReview = true;
                viewPager.setCurrentItem(i);
                updateBottomBar();
                break;
            }
        }
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int cutOffPage;
        private Fragment mPrimaryItem;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position >= listaPaginasWizard.size() ){
                return new ReviewFragment();
            }
            return listaPaginasWizard.get(position).createFragment();
        }

        @Override
        public int getItemPosition(Object object) {
            if (object == mPrimaryItem){
                return POSITION_UNCHANGED;
            }
            return POSITION_NONE;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mPrimaryItem = (Fragment) object;
        }

        @Override
        public int getCount() {
            if (listaPaginasWizard == null){
                return 0;
            }
            return Math.min(cutOffPage + 1, listaPaginasWizard.size() + 1);
        }

        public void setCutOffPage(int cutOffPage) {
            if (cutOffPage < 0) {
                cutOffPage = Integer.MAX_VALUE;
            }
            this.cutOffPage = cutOffPage;
        }

        public int getCutOffPage() {
            return this.cutOffPage;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    wizardModel.getmRootPageList().add(createPageEmail(getAccounts(this), this.wizardModel).setRequired(true));

                    onPageTreeChanged();
                    updateBottomBar();

                } else {
                    // Permission Denied
                    Toast.makeText(WizardActivity.this, "GET_ACCOUNTS Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    protected List<String> getAccounts(Context context){
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(context).getAccounts();
        HashSet<String> emails = new HashSet<>();
        // TODO ronan.lima: decidir em reunião, se mantemos o filtro de e-mail abaixo.
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches() && account.name.matches(".*[gmail|hotmail]")) {
                emails.add(account.name);
            }
        }
        List<String> lista = new ArrayList<>();
        lista.addAll(emails);
        return lista;
    }

    protected SingleFixedChoicePage createPageEmail(List<String> listaEmails, ModelCallbacks modelCallbacks){
        SingleFixedChoicePage page = new SingleFixedChoicePage(modelCallbacks, "Email");
        page.setChoices(listaEmails);
        return page;
    }

    protected void habilitaFloatButton(){
        floatButton = (FloatingActionButton) findViewById(R.id.fab);
        floatButton.hide(false);
        floatButton.setVisibility(View.VISIBLE);
        floatButton.setShowShadow(true);
        floatButton.animate().start();
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmailInformado();
            }
        });
    }

    /* Abrir dialog para o usuário informar o e-mail manualmente. */
    protected void getEmailInformado(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Informe seu e-mail");
        final EditText inputEmail = new EditText(this);
        inputEmail.setTextColor(getResources().getColor(R.color.text_light));
        inputEmail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(inputEmail);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isValidEmail(inputEmail.getText().toString())) {
                    Toast.makeText(getBaseContext(), "E-mail inválido!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    emailManual = inputEmail.getText().toString();
                    Page pgEmail = wizardModel.getmRootPageList().get(3);
                    if (pgEmail instanceof SingleFixedChoicePage) {
                        ((SingleFixedChoicePage) pgEmail).setChoices(Arrays.asList(emailManual));
                    }
                    //TODO ronan.lima: criar forma de alterar os dados da tela e já prosseguir para a próxima.
                    floatButton.setVisibility(View.GONE);
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(WizardActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancelar", null)
                .create()
                .show();
    }


}
