package com.schoolkeepa.dust.presentation.manual

import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.schoolkeepa.dust.databinding.ActivityManualBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bouncycastle.crypto.params.Blake3Parameters.context


class ManualActivity : AppCompatActivity(), OnPageChangeListener,
    OnLoadCompleteListener, OnPageErrorListener {

    private val viewModel by viewModels<ManualDetailViewModel>()

    private val TAG = "ManualActivity"

    private lateinit var binding: ActivityManualBinding

    var pdfView: PDFView? = null

    var uri: Uri? = null

    var pageNumber = 0

    var pdfFileName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityManualBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val pdfName = intent.getStringExtra("pdf")
        val searchQuery = intent.getStringExtra("search_query")

        binding.titleTextView.text = title

        pdfView = binding.pdfView

        window.statusBarColor = Color.WHITE
        observeData()
        initView()

        afterViews(pdfName!!, searchQuery!!)

    }

    private fun search(query: String) = with(binding) {
        println("EXECUTE")
        CoroutineScope(Dispatchers.Main).launch {
            val tmp = pdfView.pdfFile?.searchList(query)

            println("@@@@@@TMP $tmp")
            val a = tmp?.let {
                it.map {
                    it.pageNumber
                }
            }

            a?.let { it1 -> viewModel.setSearchList(it1) }
            pdfView.setSearchQuery(query)

            delay(500L)
            if (tmp.isNullOrEmpty()) {
                Toast.makeText(
                    this@ManualActivity,
                    "검색 결과가 없습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            closeKeyboard()
        }
    }

    private fun closeKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
    }

    private fun observeData() = with(binding) {
        viewModel.searchList.observe(this@ManualActivity) {
            if (it.isNotEmpty()) {
                totalCount.text = it.size.toString()
                viewModel.setSearchCount(1)
                pdfView.jumpTo(viewModel.searchList.value!!.first(), true)
            }
        }
        viewModel.searchCount.observe(this@ManualActivity) {
            if (!viewModel.searchList.value.isNullOrEmpty()) {
                currentCount.text = it.toString()
                pdfView.jumpTo(viewModel.searchList.value!![it - 1], true)
            }
        }
    }

    private fun initView() = with(binding) {

        backBtn.setOnClickListener {
            finish()
        }

        upBtn.setOnClickListener {
            if (viewModel.searchCount.value!! > 1) {
                viewModel.setSearchPrev()
            }
        }

        downBtn.setOnClickListener {
            if (viewModel.searchCount.value!! < viewModel.searchList.value!!.size) {
                viewModel.setSearchNext()
            }
        }

        searchBtn.setOnClickListener {
            if (searchEditText.text.isNotEmpty()) {
                search(searchEditText.text.toString())
            }
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrEmpty()) {
                    searchBtn.visibility = View.VISIBLE
                } else {
                    searchBtn.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })

        searchEditText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                p0?.let {
                    if (!it.text.isNullOrEmpty()) {
                        search(it.text.toString())
                        return true
                    }
                }
                return true
            }
        })

        cancelButton.setOnClickListener {
            searchEditText.setText("")
            viewModel.setSearchList(listOf())
            searchLayout.visibility = View.GONE
            titleLayout.visibility = View.VISIBLE
            seekLayout.visibility = View.GONE
            closeKeyboard()
        }

        searchIconButton.setOnClickListener {
            searchLayoutVisible()
            searchEditText.requestFocus()
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun afterViews(pdfName: String, searchQuery: String) {
        pdfView!!.setBackgroundColor(Color.LTGRAY)
        if (uri == null) {
            displayFromAsset(pdfName, searchQuery)
        }
        title = pdfFileName
    }

    private fun displayFromAsset(assetFileName: String, searchQuery: String) {
        pdfFileName = assetFileName
        pdfView!!.fromAsset(assetFileName)
            .defaultPage(pageNumber).onPageChange(this)
            .enableAnnotationRendering(true).onLoad(this)
            .spacing(0) // in dp
            .defaultPage(0)
            .textHighlightColor(Color.parseColor("#B9F2D5")) //                .scrollHandle(null)
            .onPageChange { page: Int, pageCount: Int ->
                Log
                    .e(
                        "com.github.barteksc.sample.PDFViewActivity.TAG",
                        "Current page is $page of $pageCount"
                    )
            } //                .nightMode(true)
            .onPageError(this).pageFitPolicy(FitPolicy.BOTH)
            .onPageBitmapRendered { pageIndex: Int, bitmap: Bitmap? ->
                Log.e(
                    "   com.github.barteksc.sample.PDFViewActivity.TAG",
                    "Page $pageIndex bitmap rendered"
                )
            }.load()
        if (searchQuery.isNotEmpty()) {
            CoroutineScope(Dispatchers.Main).launch {
                searchLayoutVisible()
                binding.searchEditText.setText(searchQuery)
                delay(1000L)
                search(searchQuery)
            }
        }
    }

    override fun loadComplete(nbPages: Int) {
        val meta = pdfView!!.getDocumentMeta()
        if (meta != null) {
            Log.e(TAG, "title = " + meta.title)
            Log.e(TAG, "author = " + meta.author)
            Log.e(TAG, "subject = " + meta.subject)
            Log.e(TAG, "keywords = " + meta.keywords)
            Log.e(TAG, "creator = " + meta.creator)
            Log.e(TAG, "producer = " + meta.producer)
            Log.e(
                TAG,
                "creationDate = " + meta.creationDate
            )
            Log.e(TAG, "modDate = " + meta.modDate)
        }
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        pageNumber = page
        title = String.format("%s %s / %s", pdfFileName, page + 1, pageCount)
    }

    override fun onPageError(page: Int, t: Throwable?) {
        Log.e(TAG, "Cannot load page $page")
    }

    private fun searchLayoutVisible() = with(binding) {
        seekLayout.visibility = View.VISIBLE
        searchLayout.visibility = View.VISIBLE
        titleLayout.visibility = View.GONE
    }
}