package com.muiezarif.kidsfitness.utils

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun toast(context: Context, message:String){
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}
fun disableScreen(window: Window){
    window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
}
fun enableScreen(window:Window){
    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
}
fun disableScreen1(window: Window?){
    window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
}
fun enableScreen1(window:Window?){
    window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
}
fun getTimeDifference(d1:String):Int{
    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    var date1 = simpleDateFormat.parse(d1)
    var date2 = Calendar.getInstance().getTime()

    val difference: Long = date2.getTime() - date1.getTime()
    var days = (difference / (1000 * 60 * 60 * 24)).toInt()
    var hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)).toInt()
    var min =
        (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours).toInt() / (1000 * 60)
    hours = if (hours < 0) -hours else hours
    return min
}
fun getFormattedDate(
    context: Context?,
    dateAndTime: String?
): String? {
    val c = Calendar.getInstance()

// set the calendar to start of today
    var dateandtime = dateAndTime?.split(" ")
    var date = dateandtime?.get(0)
    var time = dateandtime?.get(1)
    c[Calendar.HOUR_OF_DAY] = 0
    c[Calendar.MINUTE] = 0
    c[Calendar.SECOND] = 0
    c[Calendar.MILLISECOND] = 0

    val today = c.time
    val todayInMillis = c.timeInMillis
    val year = date?.split("-")?.get(0)?.toInt()
    val month = date?.split("-")?.get(1)?.toInt()
    val dayOfMonth = date?.split("-")?.get(2)?.toInt()

    c[Calendar.YEAR] = year!!
    c[Calendar.MONTH] = month!!
    c[Calendar.DAY_OF_MONTH] = dayOfMonth!!
    // and get that as a Date

    // and get that as a Date
    val dateSpecified = c.time
    return getFormattedDate(context,dateSpecified.time,dateSpecified,dateAndTime)
}
@SuppressLint("SimpleDateFormat")
fun getFormattedDate(context: Context?, smsTimeInMilis: Long, date:Date, dateAndTime:String?): String? {
    val smsTime = Calendar.getInstance()
    smsTime.timeInMillis = smsTimeInMilis
    val now = Calendar.getInstance()
    val dateFormatInput = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val gmt = dateFormatInput.parse(dateAndTime)
    val timeFormatString = "hh:mm a"
//    val dateTimeFormatString = "EEEE, MM-dd, hh:mm a"
    val dateTimeFormatString = "MM/dd/yyyy"
    val HOURS = 60 * 60 * 60.toLong()
//    return if (now[Calendar.DATE] == smsTime[Calendar.DATE]) {
//        var sdf = SimpleDateFormat(timeFormatString)
//        "Today " + sdf.format( gmt)
//    } else if (now[Calendar.DATE] - smsTime[Calendar.DATE] == 1) {
//        var sdf = SimpleDateFormat(timeFormatString)
//        "Yesterday " + sdf.format(gmt)
//    } else if (now[Calendar.YEAR] == smsTime[Calendar.YEAR]) {
//        var sdf = SimpleDateFormat(dateTimeFormatString)
//        sdf.format(gmt).toString()
//    } else {
//        var sdf = SimpleDateFormat("dd-MM-yyyy, hh:mm a")
//        sdf.format(gmt).toString()
//    }
    return if (now[Calendar.DATE] == smsTime[Calendar.DATE]) {
        var sdf = SimpleDateFormat(timeFormatString)
        sdf.format( gmt)
//            "Today " + sdf.format( gmt)
//            "Today "
    } else if (now[Calendar.DATE] - smsTime[Calendar.DATE] == 1) {
        var sdf = SimpleDateFormat(timeFormatString)
//            "Yesterday " + sdf.format(gmt)
        "Yesterday "
    } else if (now[Calendar.YEAR] == smsTime[Calendar.YEAR]) {
        var sdf = SimpleDateFormat(dateTimeFormatString)
        sdf.format(gmt).toString()
    } else {
        var sdf = SimpleDateFormat("MM/dd/yyyy")
        sdf.format(gmt).toString()
    }
}
fun isValidEmail(target: CharSequence): Boolean {
    return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
}

fun isValidUrl(url: String): Boolean {
    val regex = "((([A-Za-z]{3,9}:(?:\\/\\/)?)(?:[-;:&=\\+\\\$,\\w]+@)?[A-Za-z0-9.-]+|(?:www.|[-;:&=\\+\\\$,\\w]+@)[A-Za-z0-9.-]+)((?:\\/[\\+~%\\/.\\w-_]*)?\\??(?:[-\\+=&;%@.\\w_]*)#?(?:[\\w]*))?)".toRegex()
    return regex.containsMatchIn(url)
}

fun getCurrentTime():String{
    var calender = Calendar.getInstance()
    var simpleDateFormat = SimpleDateFormat("hh:mm a")
    var dateTime = simpleDateFormat.format(calender.time)
    return dateTime
}

fun getFileBody(path: String?, fileName: String?): MultipartBody.Part {
    val file = File(path)
    val requestFileProfile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
    return MultipartBody.Part.createFormData(fileName.toString(), file.name, requestFileProfile)
}