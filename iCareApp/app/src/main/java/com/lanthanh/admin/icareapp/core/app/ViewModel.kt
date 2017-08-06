package com.lanthanh.admin.icareapp.core.app

import io.reactivex.disposables.CompositeDisposable

/**
 * @author longv
 * Created on 03-Aug-17.
 */
interface ViewModel {
    fun resume ()
    fun pause ()
    fun setupView ()
}