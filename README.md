> 项目的业务逻辑不断变更，需求也不断扩张，导致在原有代码基础上去处理也变得越来越繁琐，这时项目框架和代码规范就显得非常必要了。Google 在 GitHub 上的 [Android Architecture Blueprints](https://github.com/googlesamples/android-architecture) 提供了很多框架 Demo，现在对其中的 [todo‑mvp](https://github.com/googlesamples/android-architecture/tree/todo-mvp/) 做一个简单的分析，并模仿写一个简单的 Demo。    

### 一、MVP 基本结构   

MVP 从更早的 MVC 框架演变过来，与 MVC 有一定的相似性：Controller/Presenter 负责逻辑的处理，Model  提供数据，View 负责显示。在 Android 上可以大大减轻 Activity 的负担，同时增加扩展性和耦合度。   

<img src="http://www.ionesmile.com/images/android/mvc_mvp_construct_contrast.png"/>

- View：负责绘制 UI 元素、与用户进行交互(在 Android 中体现为 Activity)
- Model：负责存储、检索、操纵数据(有时也实现一个 Model interface 用来降低耦合)
- Presenter：作为 View 与 Model 交互的中间纽带，处理与用户交互的负责逻辑


### 二、TodoMVP 框架结构   

<img src="http://www.ionesmile.com/images/android/todo_mvp_demo_main_construct.png"/>   

有如下几个步骤：  
1. 在 Activity 中初始化 Fragment 和 Presenter 的实例，并给 Fragment 设置 Presenter   
2. Fragment 实现了 View 的接口，并拥有 Presenter 的引用，Fragment 的非 UI 操作都通过调用 Presenter 来实现，同时 Presenter 把处理好的结果通过回调 View 回传给 Fragment 显示   
3. Presenter 拥有 View 的引用，具体的处理事情并回调 View 返回接口   

正因为 UI 与具体实现相分离，使 Activity 只需要根据回传状态渲染 UI，Presenter 中也只用考虑处理逻辑，将处理好的状态告知 View 即可。在应用 UI 经常变更时会有非常大的优势。   

### 三、参照写一个简单的 Demo   

写一个简单的搜索 Demo，输入关键字，在点击搜索按钮时开始搜索，并将返回结果显示出来。   

SearchContract 的实现，View 中包含搜索成功和搜索失败的回调，Presenter 中主要是搜索方法和 Activity 生命周期的方法。   

	public interface SearchContract {
	
	    interface View extends BaseView<SearchContract.Presenter> {
	
	        void onSearchSuccess(String result);
	
	        void onSearchFailure(String message);
	    }
	
	    interface Presenter extends BasePresenter {
	
	        void stop();
	
	        void startSearch(String searchKey);
	    }
	}

在 MainActivity 中实现了 SearchContract.View，并创建 SearchPresenter 实例和初始化 UI。   

	public class MainActivity extends AppCompatActivity implements SearchContract.View {
	
	    private SearchContract.Presenter mPresenter;
	
	    private EditText etSearchKey;
	    private TextView tvResult;
	
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	
	        new SearchPresenter(this, new DataManager());
	
	        etSearchKey = (EditText) findViewById(R.id.et_search_key);
	        tvResult = (TextView) findViewById(R.id.tv_result);
	
	        findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View view) {
	                String searchKey = etSearchKey.getText().toString();
	                if (TextUtils.isEmpty(searchKey)) {
	                    showToast("搜索关键字不能为空！");
	                    return;
	                }
	                mPresenter.startSearch(searchKey);
	            }
	        });
	    }
	
	    @Override
	    public void onResume() {
	        super.onResume();
	        mPresenter.start();
	    }
	
	    @Override
	    protected void onStop() {
	        super.onStop();
	        mPresenter.stop();
	    }
	
	    @Override
	    public void onSearchSuccess(String result) {
	        tvResult.setText(result);
	    }
	
	    @Override
	    public void onSearchFailure(String message) {
	        tvResult.setText(message);
	    }
	
	    @Override
	    public void setPresenter(SearchContract.Presenter presenter) {
	        mPresenter = presenter;
	    }
	
	    public void showToast(String message) {
	        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	    }
	}

创建 SearchPresenter 实现 SearchContract.Presenter 接口，其中的搜索方法通过调用 Model 层的接口获取数据，并处理完结果后将状态回调给 View。   

	public class SearchPresenter implements SearchContract.Presenter {
	
	    private SearchContract.View searchView;
	    private CompositeSubscription mCompositeSubscription;
	    private DataManager dataManager;
	    private String mResult;
	
	    public SearchPresenter(SearchContract.View searchView, DataManager dataManager) {
	        this.searchView = searchView;
	        this.dataManager = dataManager;
	
	        this.searchView.setPresenter(this);
	    }
	
	    @Override
	    public void start() {
	        mCompositeSubscription = new CompositeSubscription();
	    }
	
	    @Override
	    public void stop() {
	        if (mCompositeSubscription.hasSubscriptions()) {
	            mCompositeSubscription.unsubscribe();
	        }
	    }
	
	    @Override
	    public void startSearch(String searchKey) {
	        mCompositeSubscription.add(dataManager.getSearchResult(searchKey)
	                .subscribeOn(Schedulers.io())
	                .observeOn(AndroidSchedulers.mainThread())
	                .subscribe(new Observer<ResponseBody>() {
	                    @Override
	                    public void onCompleted() {
	                        if (mResult != null) {
	                            searchView.onSearchSuccess(mResult);
	                        }
	                    }
	
	                    @Override
	                    public void onError(Throwable e) {
	                        e.printStackTrace();
	                        searchView.onSearchFailure("请求失败！！！");
	                    }
	
	                    @Override
	                    public void onNext(ResponseBody responseBody) {
	                        if (responseBody != null) {
	                            try {
	                                mResult = responseBody.source().readUtf8();
	                            } catch (IOException e) {
	                                e.printStackTrace();
	                            }
	                        }
	                    }
	                })
	        );
	    }
	}

