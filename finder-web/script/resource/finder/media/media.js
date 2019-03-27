(function() {
    Finder.getPlayList = function() {
        var win = Finder.getWindow();
        return new win.PlayList();
    };

    Finder.getAudioPlayer = function() {
        if(this.audioPlayer == null) {
            var win = Finder.getWindow();

            if(window != win) {
                if(win.audioPlayer != null) {
                    this.audioPlayer = win.audioPlayer;
                }
                else {
                    this.audioPlayer = new win.AudioPlayer({"container": "finder-audiodialog", "title": "AudioPlayer"});
                    win.audioPlayer = this.audioPlayer;
                }
            }
            else {
                this.audioPlayer = new AudioPlayer({"container": "finder-audiodialog"});
            }

            /**
             * 浏览器拖拽事件
             */
            this.audioPlayer.drop = function(event) {
                var e = (event || window.event);
                var src = (e.srcElement || e.target);

                try {
                    var item = JSON.parse(e.dataTransfer.getData("Text"));

                    if(item == null || item.file != 1) {
                        return;
                    }

                    var ext = FileType.getExtension(item.url).toLowerCase();

                    if(ext == "mp3") {
                        this.add(item, false);
                    }
                }
                catch(e) {
                    console.log(e.name + ": " + e.message);
                }
            };
        }
        return this.audioPlayer;
    };

    Finder.getVideoPlayer = function() {
        if(this.videoPlayer == null) {
            var win = Finder.getWindow();

            if(window != win) {
                if(win.videoPlayer != null) {
                    this.videoPlayer = win.videoPlayer;
                }
                else {
                    this.videoPlayer = new win.VideoPlayer({"container": "finder-videodialog", "title": "VideoPlayer"});
                    win.videoPlayer = this.videoPlayer;
                }
            }
            else {
                this.videoPlayer = new VideoPlayer({"container": "finder-videodialog"});
            }
        }
        return this.videoPlayer;
    };

    FileType.registe("mp3", function(file, options) {
        var index = 0;
        var host = Finder.getHost();
        var workspace = Finder.getWorkspace();
        var path = Finder.getPath();
        var prefix = Finder.getRequestURI() + "?action=finder.download&host=" + encodeURIComponent(host) + "&workspace=" + encodeURIComponent(workspace);
        var cover = Finder.getRequestURI() + "?action=res&path=/finder/images/hua.jpg";
        var item = {"name": file.name, "file": file.file, "url": prefix + "&path=" + encodeURIComponent(path + "/" + file.name), "cover": cover};
        var audioPlayer = Finder.getAudioPlayer();
        audioPlayer.add(item, true);
        audioPlayer.open();
        return true;
    });

    var openWindow = function(url, name, width, height, features){
        var w = width;
        var h = height;
        if(w == null) w = window.screen.availWidth;
        if(h == null) h = window.screen.availHeight;

        var x = Math.floor((screen.availWidth  - w) / 2);
        var y = Math.floor((screen.availHeight - h - 60) / 2);

        if(x < 0) {
            x = 0;
        }

        if(y < 0) {
            y = 0;
        }

        if(features == null || features == "") {
            features = "top=" + y + ",left=" + x + ",width=" + w + ",height=" + h;
        }
        else {
            features = "top=" + y + ",left=" + x + ",width=" + w + ",height=" + h + "," + features;
        }
        return window.open(url, name, features);
    };

    FileType.registe("mp4, mov, rmvb, mkv", function(file, options) {
        var index = 0;
        var host = Finder.getHost();
        var workspace = Finder.getWorkspace();
        var path = Finder.getPath();
        var fileList = Finder.getFileList();
        var prefix = Finder.getRequestURI() + "?action=finder.download&host=" + encodeURIComponent(host) + "&workspace=" + encodeURIComponent(workspace);
        var cover = Finder.getRequestURI() + "?action=res&path=/finder/images/hua.jpg";
        var playList = Finder.getPlayList();

        for(var i = 0; i < fileList.length; i++) {
            var fileName = fileList[i].name;
            var fileType = FileType.getType(fileName);

            if(FileType.contains("mp4, mov", fileType)) {
                if(fileName == file.name) {
                    index = playList.size();
                }
                playList.add({"title": fileName, "url": prefix + "&path=" + encodeURIComponent(path + "/" + fileName), "cover": cover});
            }
        }

        openWindow(Finder.getRequestURI() + "?action=finder.play&host=" + encodeURIComponent(host) + "&workspace=" + encodeURIComponent(workspace) + "&path=" + encodeURIComponent(path + "/" + file.name), "_blank", 800, 600);

        /*
        var videoPlayer = Finder.getVideoPlayer();
        videoPlayer.setPlayList(playList);
        videoPlayer.play(index);
        videoPlayer.open();
        return true;
        */
    });

    /**
     * Finder拖拽事件
     */
    Finder.on("drop", function(event, selected) {
        var src = (event.srcElement || event.target);
        var ele = jQuery(src).closest("div.play-list");

        if(ele.size() < 1) {
            return;
        }

        var audioPlayer = Finder.getAudioPlayer();
        var cover = Finder.getRequestURI() + "?action=res&path=/finder/images/hua.jpg";

        for(var i = 0; i < selected.length; i++) {
            var file = Finder.getFile(selected[i]);
            var ext = FileType.getExtension(file.name).toLowerCase();
            var path = Finder.getPath();

            if(ext == "mp3") {
                var url = Finder.getURL({"action": "finder.get", "path": path + "/" + file.name});
                var item = {"name": file.name, "url": url, "cover": cover};
                audioPlayer.add(item, false);
            }
        }
    });
})();

(function() {
    var win = Finder.getWindow();
    var doc = win.document;
    var home = Finder.getRequestURI() + "?action=res&path=";

    if(win._finder_media_loaded != true) {
        win._finder_media_loaded = true;
        ResourceLoader.css(doc, home + "/finder/media/css/media.css");
        ResourceLoader.script(doc, home + "/finder/media/index.js");
    }
})();
