package com.bmit.lib_core.base_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

/**
 * Author: created by huhuaijun on 2026/9/28 11:25
 * Function:
 */
abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> :
    Fragment() {

    private var _binding: VB? = null

    protected val binding: VB
        get() = _binding
            ?: throw IllegalStateException(
                "Binding is only valid between onCreateView and onDestroyView"
            )

    protected val viewModel: VM by lazy {
        ViewModelProvider(this)[getViewModelClass()]
    }

    private val fragmentName: String
        get() = this::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = createViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListener()
        observeData()
        initData()
    }

    /**
     * 创建 ViewBinding
     */
    abstract fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB

    /**
     * 获取 ViewModel Class
     */
    abstract fun getViewModelClass(): Class<VM>

    protected open fun initView() {
    }

    protected open fun initListener() {
    }

    protected open fun observeData() {
    }

    protected open fun initData() {
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}