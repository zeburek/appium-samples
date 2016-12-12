'use strict';

var wd = require('wd');
var request = require('request');
var Promise = require('bluebird'); // jshint ignore:line
var fs = require('fs');

var filepath = 'outputs/';


var apiToken = 'xyz'; // your API token from https://appetize.io/docs#request-api-token
var deviceType = 'iphone5s'; // iphone4s, iphone5s, iphone6s, iphone6splus, iphone7, iphone7plus, ipadair2
var publicKey = 'w6w6ng97e5ngd5kpwtg5d7d4u8'; // replace with your own publicKey after uploading through website or API
var osVersion = '9.3'; // 9.2 also supported
var proxy = 'intercept'; // false for no proxy, or specify your own with http://proxy-example.com:port

var driver = wd.remote('https://' + apiToken + '@appium.appetize.io/wd/hub', 'promiseChain');

console.log('starting session');
driver.init({
    device: deviceType,
    publicKey: publicKey,
    osVersion: osVersion,
    proxy: proxy
}).delay(5000)
.then(takeScreenshot)
.then(function() {
    console.log('tapping element');
    return driver.elementByXPath('//UIAButton[@name="Saved"]').tap().delay(2000);
})
.then(takeScreenshot)
.then(function() {
    console.log('ending session');
    return driver.quit();
})
.then(function() {
    if (proxy == 'intercept') {
        console.log('Getting HAR file');

        var url = 'https://api.appetize.io/v1/networkCapture/appiumId/' + driver.sessionID;
        console.log(url);

        request.get(url, function(err, response, body) {
            if (err) throw err;

            var har = JSON.parse(body);
            console.log('Har has ' + har.log.entries.length + ' entries');

            // write file
            var filename = 'appetize-' + deviceType + '-' + osVersion + '-' + publicKey + '-har-' + Date.now() + '.json';
            console.log('writing file to ' + filename);
            fs.writeFileSync(filepath + filename, JSON.stringify(har, null, 2));
        });
    } else {
        return;
    }

})
.catch(function(error) {
    console.log(error);
});

function takeScreenshot() {
    console.log('take screenshot');
    return driver.takeScreenshot()
    .then(function(data) {
            // write file
            var filename = 'appetize-' + deviceType + '-' + osVersion + '-' + publicKey + '-screenshot-' + Date.now() + '.png';
            console.log('writing file to ' + filename);
            fs.writeFileSync(filepath + filename, data, 'base64');
        });
}