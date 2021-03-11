console.log(`content:start`);
let JSBridge = {
    postMessage: function (message) {
        browser.runtime.sendMessage({
            action: "JSBridge",
            data: message
        });
    }
}
window.wrappedJSObject.JSBridge = cloneInto(
    JSBridge,
    window,
    { cloneFunctions: true });

browser.runtime.onMessage.addListener((data, sender) => {
    console.log("content:eval:" + data);
    if (data.action === 'evalJavascript') {
        let evalCallBack = {
            id: data.id,
            action: "evalJavascript",
        }
        try {
            let result = window.eval(data.data);
            console.log("content:eval:result" + result);
            if (result) {
                evalCallBack.data = result;
            } else {
                evalCallBack.data = "";
            }
        } catch (e) {
            evalCallBack.data = e.toString();
            return Promise.resolve(evalCallBack);
        }
        return Promise.resolve(evalCallBack);
    }
});