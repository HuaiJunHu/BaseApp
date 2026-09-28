package com.bmit.lib_core.base_ui

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Author: created by huhuaijun on 2026/9/28 11:18
 * Function:
 */
abstract class BaseActivity<VB : ViewBinding, VM : ViewModel> : AppCompatActivity() {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding
            ?: throw IllegalStateException("Binding is only valid between onCreate and onDestroy")

    protected val viewModel: VM by lazy {
        ViewModelProvider(this)[getViewModelClass()]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = createViewBinding(layoutInflater)
        setContentView(binding.root)

        initView()
        initListener()
        observeData()
        initData()
    }

    /**
     * 创建 ViewBinding
     */
    abstract fun createViewBinding(inflater: LayoutInflater): VB

    /**
     * 获取 ViewModel Class
     */
    abstract fun getViewModelClass(): Class<VM>

    /**
     * 初始化 View
     */
    protected open fun initView() {
    }

    /**
     * 初始化点击事件等
     */
    protected open fun initListener() {
    }

    /**
     * 观察 ViewModel 数据
     */
    protected open fun observeData() {
    }

    /**
     * 初始化数据
     */
    protected open fun initData() {
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}