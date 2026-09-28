package com.bmit.lib_core.base_ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.bmit.lib_core.utils.LogUtil

/**
 * Author: created by huhuaijun on 2026/9/28 11:18
 * Function:
 */
abstract class BaseActivity<VB : ViewBinding, VM : ViewModel> : AppCompatActivity() {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding
            ?: throw IllegalStateException("Binding is only valid between onCreate and onDestroy")

    private val activityName: String
        get() = this::class.java.simpleName


    protected val viewModel: VM by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this)[getViewModelClass()]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.d("onCreate: $activityName")
        _binding = createViewBinding(layoutInflater)
        setContentView(binding.root)

        initView()
        initListener()
        initObserver()
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
    protected open fun initObserver() {
    }

    /**
     * 初始化数据
     */
    protected open fun initData() {
    }

    override fun onResume(){
        LogUtil.d("onResume: $activityName")
        super.onResume()
    }

    override fun onNewIntent(intent: Intent) {
        LogUtil.d("onNewIntent: $activityName")
        super.onNewIntent(intent)
    }

    override fun onDestroy() {
        _binding = null
        LogUtil.d("onDestroy: $activityName")
        super.onDestroy()
    }
}