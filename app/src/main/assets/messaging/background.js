'use strict';
const port = browser.runtime.connectNative("browser");

async function sendMessageToTab(message) {
 try {
   let tabs = await browser.tabs.query({})
   console.log(`background:tabs:${tabs}`)
   return await browser.tabs.sendMessage(
     tabs[tabs.length - 1].id,
     message
   )
 } catch (e) {
   console.log(`background:sendMessageToTab:req:error:${e}`)
   return e.toString();
 }
}
//监听 app message
port.onMessage.addListener(request => {
 let action = request.action;
 if(action === "evalJavascript") {
     sendMessageToTab(request).then((resp) => {
       port.postMessage(resp);
     }).catch((e) => {
       console.log(`background:sendMessageToTab:resp:error:${e}`)
     });
   }
})

//接收 content.js message
browser.runtime.onMessage.addListener((data, sender) => {
   let action = data.action;
   console.log("background:content:onMessage:" + action);
   if (action === 'JSBridge') {
       port.postMessage(data);
   }
   return Promise.resolve('done');
})
