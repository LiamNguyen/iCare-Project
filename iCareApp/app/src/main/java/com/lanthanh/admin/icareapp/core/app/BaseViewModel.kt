package com.lanthanh.admin.icareapp.core.app

import com.lanthanh.admin.icareapp.core.mvvm.MVVMViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * @author longv
 * Created on 03-Aug-17.
 */
abstract class BaseViewModel : MVVMViewModel {

    protected val disposables: CompositeDisposable = CompositeDisposable()

    override fun pause () {
        if (!disposables.isDisposed) disposables.dispose()
    }
}