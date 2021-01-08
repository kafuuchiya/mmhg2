#  mmg2
基於學習與研究的目的，使用UIWebView包裝的一個簡單的Hyper App (Android)。

以下網站作為主要目標：

https://m.manhuagui.com/

該網站本身有進行responsive設計，但對於觀看漫畫方面還不足夠，所以進行一些補充

## 主要内容

* 廣告內容的隱藏顯示
* loading圖片及旋轉動畫
* 網頁加載未完成時，顯示loading圖標
* 鎖定“漫畫大全”頁面顯示條件為“日本”及“最近更新”
* 一些UI/UX的優化設計

    由於web app返回上一頁是基於瀏覽器訪問的URL歷史，例如，觀看某漫畫到某一頁，點擊back按鈕，如果是native app會返回漫畫目錄，但web app只會返回漫畫上一頁。針對此類頁面跳轉问题進行優化。

    * 觀看漫畫時，點擊back按鈕，返回該漫畫目錄

    * 觀看漫畫時，自動橫屏顯示，增加觀感。其他情況自動轉回豎屏

    * 首頁點擊手机BACK按钮，彈出Alert Dialog Box，詢問是否退出應用

    * 由於back按鈕可以直接返回目錄，所以去除漫畫目錄及漫畫頁面頂部及底部導航，使頁面重要內容更加突顯

## 相關技術

**IDE**: Android studio

**Language**: Java, JavaScript, CSS, HTML, jQuery

* 新知識

    * Android及Js之間的交互，例如Js呼喚Android的method

    * 使用Mutation Observer API 用來監視DOM變動。DOM的任何變動，例如節點的增刪，這個 API 都可以得到通知。用於網頁數據更新以及顯示完成時，獲得callback，再進行下一步操作。

## 可能會有

* 漫畫列表頁面“to top”及“refresh”按鈕
