package com.schoolkeepa.dust.data

interface Downloader {

    fun downloadFile(url: String): Long
}