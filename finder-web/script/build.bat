@echo off

@IF not exist "release" mkdir release
@IF not exist "release\finder\media" mkdir "release\finder\media"

jsmin < "resource\finder\widget.js"      >  "release/finder/widget.min.js"          "(c)2018 www.finderweb.net"
jsmin < "resource\finder\finder.js"      >  "release/finder/finder.min.js"          "(c)2018 www.finderweb.net"
jsmin < "resource\finder\grep.js"        >  "release/finder/grep.min.js"            "(c)2018 www.finderweb.net"
jsmin < "resource\finder\less.js"        >  "release/finder/less.min.js"            "(c)2018 www.finderweb.net"
jsmin < "resource\finder\tail.js"        >  "release/finder/tail.min.js"            "(c)2018 www.finderweb.net"
jsmin < "resource\finder\base64.js"      >  "release/finder/base64.min.js"          "(c)2018 www.finderweb.net"
jsmin < "resource\finder\charset.js"     >  "release/finder/charset.min.js"         "(c)2018 www.finderweb.net"
jsmin < "resource\finder\colorpicker.js" >  "release/finder/colorpicker.min.js"     "(c)2018 www.finderweb.net"
jsmin < "resource\finder\config.js"      >  "release/finder/config.min.js"          "(c)2018 www.finderweb.net"
jsmin < "resource\finder\display.js"     >  "release/finder/display.min.js"         "(c)2018 www.finderweb.net"
jsmin < "resource\finder\fileupload.js"  >  "release/finder/fileupload.min.js"      "(c)2018 www.finderweb.net"
jsmin < "resource\finder\index.js"       >  "release/finder/index.min.js"           "(c)2018 www.finderweb.net"
jsmin < "resource\finder\password.js"    >  "release/finder/password.min.js"        "(c)2018 www.finderweb.net"
jsmin < "resource\finder\scrollpage.js"  >  "release/finder/scrollpage.min.js"      "(c)2018 www.finderweb.net"
jsmin < "resource\finder\signin.js"      >  "release/finder/signin.min.js"          "(c)2018 www.finderweb.net"
jsmin < "resource\finder\tab.js"         >  "release/finder/tab.min.js"             "(c)2018 www.finderweb.net"
jsmin < "resource\finder\tree.js"        >  "release/finder/tree.min.js"            "(c)2018 www.finderweb.net"
jsmin < "resource\finder\split.js"       >  "release/finder/split.min.js"           "(c)2018 www.finderweb.net"
jsmin < "resource\finder\zip-viewer.js"  >  "release/finder/zip-viewer.min.js"      "(c)2018 www.finderweb.net"
jsmin < "resource\finder\img-viewer.js"  >  "release/finder/img-viewer.min.js"      "(c)2018 www.finderweb.net"
jsmin < "resource\finder\editor.js"      >  "release/finder/editor.min.js"          "(c)2018 www.finderweb.net"
jsmin < "resource\finder\editplus.js"    > "release/finder/editplus.min.js"         "(c)2018 www.finderweb.net"

@rem
jsmin < "resource\finder\media\index.js" >  "release/finder/media/index.min.js"     "(c)2018 www.finderweb.net"
jsmin < "resource\finder\media\media.js" >  "release/finder/media/media.min.js"     "(c)2018 www.finderweb.net"
@pause
