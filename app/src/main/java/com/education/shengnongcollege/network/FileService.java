package com.education.shengnongcollege.network;//package com.education.shengnongcollege.network;
//
//import android.content.Context;
//
//import com.education.shengnongcollege.network.model.HttpMethodType;
//import com.education.shengnongcollege.network.utils.RetrofitUtil;
//
//import java.io.File;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Set;
//
//import io.reactivex.Observable;
//import io.reactivex.Observer;
//import io.reactivex.disposables.Disposable;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.RequestBody;
//import okhttp3.ResponseBody;
//import retrofit2.http.Body;
//import retrofit2.http.HeaderMap;
//import retrofit2.http.Multipart;
//import retrofit2.http.POST;
//import retrofit2.http.PUT;
//import retrofit2.http.Part;
//import retrofit2.http.PartMap;
//import retrofit2.http.Url;
//
///**
// * Created by wuweixiang on 17/6/28.
// */
//
//public class FileService {
//    private Context context;
//    private InFileService inFileService;
//
//    public FileService(Context context, String baseUrl) {
//        this.context = context;
//        inFileService = new RetrofitUtil(context).getRetrofit(baseUrl, false).create(InFileService.class);
//    }
//
//    public <T> Disposable pTUploadSingleFile(HttpMethodType methodType, Class<T> tClass,
//                                             Observer<T> subscriber,
//                                             String url,
//                                             Map<String, String> extraParams,
//                                             String fileKey, File file) {
//
//        //基础header
//        HashMap<String, String> baseHeader = ActionUtil.getCommonHeaderMap_APPVersion_ClientInfo();
//        baseHeader.put("Token", BaseCommonUtil.getToken());
//        if (extraParams.containsKey("action")) {
//            baseHeader.put("Action", extraParams.get("action"));
//        }
//       return uploadSingleFile(methodType, tClass, subscriber, url, baseHeader, extraParams, fileKey, file);
//    }
//
//    public <T> Disposable uploadSingleFile(HttpMethodType methodType, Class<T> tClass,
//                                     Observer<T> subscriber,
//                                     String url,
//                                     Map<String, String> header,
//                                     Map<String, String> extraParams,
//                                     String fileKey, File file) {
//        RequestBody requestFile =
//                RequestBody.create(MediaType.parse("application/octet-stream"), file);
//        // MultipartBody.Part is used to send also the actual file name
//        MultipartBody.Part filePart = MultipartBody.Part.createFormData(fileKey, file.getName(), requestFile);
//        Map<String, RequestBody> paramsPart = new HashMap<>();
//        if (extraParams != null) {
//            Set<String> paramsKey = extraParams.keySet();
//            Iterator<String> iterator = paramsKey.iterator();
//            while (iterator.hasNext()) {
//                String key = iterator.next();
//                String value = extraParams.get(key);
//                RequestBody paramBody = RequestBody.create(
//                        MediaType.parse("text/plain"), value);
//                paramsPart.put(key, paramBody);
//            }
//        }
//
//
//        if (header == null) {
//            header = new HashMap<>();
//        }
//
//        Observable<ResponseBody> observable = null;
//        if (methodType == HttpMethodType.POST) {
//            observable = inFileService.postUploadOneFile(url, header,
//                    paramsPart, filePart);
//        } else if (methodType == HttpMethodType.PUT) {
//            observable = inFileService.putUploadOneFile(url, header,
//                    paramsPart, filePart);
//        }
//        if (observable == null) {
//            JkysLog.e("异常", "不支持的HttpMethodType");
//            return null;
//        }
//        return NetworkController.getInstance(context).gwApiCallGenic(tClass, subscriber, observable);
//    }
//
//    public <T> Disposable postPTUploadMultiFile(Class<T> tClass,
//                                                Observer<T> subscriber,
//                                                String url,
//                                                Map<String, String> extraParams,
//                                                Map<String, File> fileMap) {
//        //基础header
//        HashMap<String, String> baseHeader = ActionUtil.getCommonHeaderMap_APPVersion_ClientInfo();
//        baseHeader.put("Token", BaseCommonUtil.getToken());
//        if (extraParams.containsKey("action")) {
//            baseHeader.put("Action", extraParams.get("action"));
//        }
//        return postUploadMultiFile(tClass, subscriber, url, baseHeader, extraParams, fileMap);
//    }
//
//    private <T> Disposable postUploadMultiFile(Class<T> tClass,
//                                         Observer<T> subscriber,
//                                         String url,
//                                         Map<String, String> header,
//                                         Map<String, String> extraParams,
//                                         Map<String, File> fileMap) {
//        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        if (header == null)
//            header = new HashMap<>();
//        //文件
//        if (fileMap != null) {
//            Set<Map.Entry<String, File>> fileSetEntry = fileMap.entrySet();
//            Iterator iteratorFile = fileSetEntry.iterator();
//            while (iteratorFile.hasNext()) {
//                Map.Entry<String, File> entry = (Map.Entry<String, File>) iteratorFile.next();
//                File file = entry.getValue();
//                builder.addFormDataPart(entry.getKey(), file.getName(),
//                        RequestBody.create(MediaType.parse("application/octet-stream"), file));
//            }
//        }
//        //附加参数
//        if (extraParams != null) {
//            Set<Map.Entry<String, String>> extraSetEntry = extraParams.entrySet();
//            Iterator iterator = extraSetEntry.iterator();
//            while (iterator.hasNext()) {
//                Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
//                builder.addFormDataPart(entry.getKey(), entry.getValue());
//            }
//        }
//
//        RequestBody requestBody = builder.build();
//        Observable<ResponseBody> observable = inFileService.postUploadMultiFile(url, header, requestBody);
//        return NetworkController.getInstance(context).gwApiCallGenic(tClass, subscriber, observable);
//    }
////<<<<<<< HEAD:jkysnetwork/src/main/java/com/jkys/jkysnetwork/FileService.java
////=======
//
////    public <T> void putPTUploadMultiFile(Class<T> tClass,
////                                         Subscriber<T> subscriber,
////                                         String url,
////                                         Map<String, String> extraParams,
////                                         Map<String, File> fileMap) {
////        //基础header
////        HashMap<String, String> baseHeader = ActionUtil.getCommonHeaderMap_APPVersion_ClientInfo();
////        baseHeader.put("Token", BaseCommonUtil.getToken());
////        if (extraParams.containsKey("action")) {
////            baseHeader.put("Action", extraParams.get("action"));
////        }
////        putUploadMultiFile(tClass, subscriber, url, baseHeader, extraParams, fileMap);
////    }
//
////    public <T> void putUploadMultiFile(Class<T> tClass,
////                                       Subscriber<T> subscriber,
////                                       String url,
////                                       Map<String, String> header,
////                                       Map<String, String> extraParams,
////                                       Map<String, File> fileMap) {
////        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
////        if (header == null)
////            header = new HashMap<>();
////        //文件
////        if (fileMap != null) {
////            Set<Map.Entry<String, File>> fileSetEntry = fileMap.entrySet();
////            Iterator iteratorFile = fileSetEntry.iterator();
////            while (iteratorFile.hasNext()) {
////                Map.Entry<String, File> entry = (Map.Entry<String, File>) iteratorFile.next();
////                File file = entry.getValue();
////                builder.addFormDataPart(entry.getKey(), file.getName(),
////                        RequestBody.create(MediaType.parse("application/octet-stream"), file));
////            }
////        }
////        //附加参数
////        if (extraParams != null) {
////            Set<Map.Entry<String, String>> extraSetEntry = extraParams.entrySet();
////            Iterator iterator = extraSetEntry.iterator();
////            while (iterator.hasNext()) {
////                Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
////                builder.addFormDataPart(entry.getKey(), entry.getValue());
////            }
////        }
////
////        RequestBody requestBody = builder.build();
////        Observable<ResponseBody> observable = inFileService.putUploadMultiFile(url, header, requestBody);
////        NetworkController.getInstance(context).gwApiCallGenic(tClass, subscriber, observable);
////    }
////>>>>>>> qa_release:jkysGWApi/src/main/java/com/jkys/api/gw/FileService.java
//
////    public <T> void putPTUploadMultiFile(Class<T> tClass,
////                                         Subscriber<T> subscriber,
////                                         String url,
////                                         Map<String, String> extraParams,
////                                         Map<String, File> fileMap) {
////        //基础header
////        HashMap<String, String> baseHeader = ActionUtil.getCommonHeaderMap_APPVersion_ClientInfo();
////        baseHeader.put("Token", BaseCommonUtil.getToken());
////        if (extraParams.containsKey("action")) {
////            baseHeader.put("Action", extraParams.get("action"));
////        }
////        putUploadMultiFile(tClass, subscriber, url, baseHeader, extraParams, fileMap);
////    }
//
////    public <T> void putUploadMultiFile(Class<T> tClass,
////                                       Subscriber<T> subscriber,
////                                       String url,
////                                       Map<String, String> header,
////                                       Map<String, String> extraParams,
////                                       Map<String, File> fileMap) {
////        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
////        if (header == null)
////            header = new HashMap<>();
////        //文件
////        if (fileMap != null) {
////            Set<Map.Entry<String, File>> fileSetEntry = fileMap.entrySet();
////            Iterator iteratorFile = fileSetEntry.iterator();
////            while (iteratorFile.hasNext()) {
////                Map.Entry<String, File> entry = (Map.Entry<String, File>) iteratorFile.next();
////                File file = entry.getValue();
////                builder.addFormDataPart(entry.getKey(), file.getName(),
////                        RequestBody.create(MediaType.parse("application/octet-stream"), file));
////            }
////        }
////        //附加参数
////        if (extraParams != null) {
////            Set<Map.Entry<String, String>> extraSetEntry = extraParams.entrySet();
////            Iterator iterator = extraSetEntry.iterator();
////            while (iterator.hasNext()) {
////                Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
////                builder.addFormDataPart(entry.getKey(), entry.getValue());
////            }
////        }
////
////        RequestBody requestBody = builder.build();
////        Observable<ResponseBody> observable = inFileService.putUploadMultiFile(url, header, requestBody);
////        NetworkController.getInstance(context).gwApiCallGenic(tClass, subscriber, observable);
////    }
//
//
//    public interface InFileService {
//        //响应不支持泛型,故只能先返回ResponseBody,再转换
//        //上传一个文件
//        @Multipart
//        @PUT()
//        Observable<ResponseBody> putUploadOneFile(@Url String url,
//                                                  @HeaderMap Map<String, String> header,
//                                                  @PartMap Map<String, RequestBody> extraParams,
//                                                  @Part MultipartBody.Part file
//        );
//
////<<<<<<< HEAD:jkysnetwork/src/main/java/com/jkys/jkysnetwork/FileService.java
//        @Multipart
//        @POST()
//        Observable<ResponseBody> postUploadOneFile(@Url String url,
//                                                   @HeaderMap Map<String, String> header,
//                                                   @PartMap Map<String, RequestBody> extraParams,
//                                                   @Part MultipartBody.Part file
//        );
//
//        @POST()
//        Observable<ResponseBody> postUploadMultiFile(@Url String url,
//                                                     @HeaderMap Map<String, String> header,
//                                                     @Body RequestBody requestBody
////=======
////        @POST()
////        Observable<ResponseBody> postUploadMultiFile(@Url String url,
////                                                    @HeaderMap Map<String, String> header,
////                                                    @Body RequestBody requestBody
////>>>>>>> qa_release:jkysGWApi/src/main/java/com/jkys/api/gw/FileService.java
//        );
//    }
//}
