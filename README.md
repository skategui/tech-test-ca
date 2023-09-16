# MVI + Compose + Coroutine Flow For CA

Technical tests for CA.

### Architecture, language and tools used to make this app 💪

- 🔨Kotlin : Language of dev
- ♻️ MVI (Model View Intent) : An unidirectional flow + a reducer to create the new State. 
- 🧹 Clean architecture : To well structured the app. 
- 💉 Koin : to inject deps. 
- 📈 Coroutine (with Flow) : to make the app fully reactive.
- 🛰️ Retrofit : To make HTTP requests.
- 👀 JUnit with Turbine and Mockk : to cover the code of unit tests !
- 🖼️ Jetpack Compose
- 🔒 Proguard : to obfuscate and minify the code.
- Architecture : usage of multi module in a mono repo

### Improvements that could be made in the future 💡

- Security : setup SSL pinning and certificate transparency to avoid any man in the middle
- Tracking : Add a tool to track user interactions in the app (segment / mixpanel + datadog) to understand how the users use the app and verify if the usage is normal
- Network : Cache the requests **smartly** with a different expire time given the resource to access.
- Network : Add some paging on the resources that intent to return a big list of data. To avoid the user to wait too long and **use less memory/less battery**.
- Network : **Take in consideration the connexion speed** to load a different amount of data per request, and make sure the user does not wait too long to get the response of the request.
- Network :  use https://developers.google.com/protocol-buffers/ or https://google.github.io/flatbuffers/ to **transit smaller on-the-wire packet size, then make the requests faster**.
  Benchmark of the performance : https://codeburst.io/json-vs-protocol-buffers-vs-flatbuffers-a4247f8bda6f
- UI: Setup dark mode
- Error handling : better error handling / offline management
- CI : Add a CI to build + run the unit tests after each commit / add a nightly build that will run all the tests (junit + espresso, as it's longer to run).
- ....

### Demo 🔥


<video width="630" height="300" src="https://github.com/skategui/tech-test-ca/assets/2959509/39d84928-f98e-47f6-99cf-70c993d38cf3.mp4"></video>
<img width="342" alt="banks" src="https://github.com/skategui/tech-test-ca/assets/2959509/5cd9bc73-b9ad-4997-b661-72e0dda8cd9c">
<img width="341" alt="operations" src="https://github.com/skategui/tech-test-ca/assets/2959509/59cb5741-cfd0-4313-aa52-eef8174fd12f">



By Guillaume Agis - Sept 2023

With love and passion <3
