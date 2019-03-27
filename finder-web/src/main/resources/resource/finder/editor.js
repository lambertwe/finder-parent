// (c)2018 www.finderweb.net

(function(){var com={skin:{framework:{}}};var Class=com.skin.framework.Class={};Class.getClassId=function(){if(this.id==null){this.id=0;}
this.id++;return"class_"+this.id;};Class.create=function(parent,constructor,fields){var clazz=null;if(parent!=null){if(constructor!=null){clazz=function(){parent.apply(this,arguments);constructor.apply(this,arguments);};}
else{clazz=function(){parent.apply(this,arguments);};}
for(var property in parent.prototype){clazz.prototype[property]=parent.prototype[property];}
clazz.prototype["toString"]=parent.prototype["toString"];clazz.$super=parent.prototype;}
else{if(constructor!=null){clazz=function(){constructor.apply(this,arguments);};}
else{clazz=function(){};}
clazz.$super={};}
clazz.parent=parent;clazz.classId=this.getClassId();clazz.prototype.constructor=clazz;if(fields!=null&&fields.length>0){this.let(clazz,fields);}
return clazz;};Class.$super=function(instance,prototype){var object={};for(var i in prototype){if(typeof(prototype[i])=="function"){object[i]=function(){prototype[i].apply(instance,arguments);};}}
return object;};Class.getInstance=function(parent,constructor){return new(Class.create(parent,constructor))();};Class.extend=function(child,parent){if(child==null){child={};}
for(var property in parent){child[property]=parent[property];}
return child;};Class.let=function(f,fields){var p=f.prototype;for(var i=0,length=fields.length;i<length;i++){var name=fields[i];var method=name.charAt(0).toUpperCase()+name.substring(1);p["set"+method]=new Function(name,"this."+name+" = "+name+";");p["get"+method]=new Function("return this."+name+";");}};var StringUtil={};StringUtil.trim=function(s){return(s!=null?new String(s).replace(/(^\s*)|(\s*$)/g,""):"");};StringUtil.startsWith=function(source,search){if(source.length>=search.length){return(source.substring(0,search.length)==search);}
return false;};StringUtil.replace=function(source,search,replacement){if(source==null){return"";}
if(search==null||search.length<1){return source;}
if(replacement==null){replacement="";}
var s=0;var e=0;var d=search.length;var buffer=[];while(true){e=source.indexOf(search,s);if(e==-1){buffer[buffer.length]=source.substring(s);break;}
buffer[buffer.length]=source.substring(s,e);buffer[buffer.length]=replacement;s=e+d;}
return buffer.join("");};var EventUtil={};EventUtil.bind=function(object,handler){return function(event){return handler.call(object,(event||window.event));};};EventUtil.fire=function(element,type,bubbles,cancelable){if(document.createEvent){var event=document.createEvent("Event");event.initEvent(type,bubbles!==undefined?bubbles:true,cancelable!==undefined?cancelable:false);element.dispatchEvent(event);}
else if(document.createEventObject){var event=document.createEventObject();element.fireEvent("on"+type,event);}
else if(typeof(element["on"+type])=="function"){element["on"+type]();}};EventUtil.addListener=function(target,type,handler){var a=type.split(",");for(var i=0;i<a.length;i++){if(target.attachEvent){target.attachEvent("on"+a[i],handler);}
else if(target.addEventListener){target.addEventListener(a[i],handler,false);}
else{target["on"+a[i]]=handler;}}};EventUtil.removeListener=function(target,type,handler){if(target.detachEvent){target.detachEvent("on"+type,handler);}
else if(target.removeEventListener){target.removeEventListener(type,handler,false);}
else{target["on"+type]=null;}};EventUtil.cancel=function(event){if(event==null){return false;}
if(event.stop){event.stop();}
else if(event.stopPropagation()){event.stopPropagation();}
else{event.cancelBubble=true;}
if(event.preventDefault){event.preventDefault();}
event.cancel=true;event.returnValue=false;return false;};var Html={};Html.encode=function(s){if(s==null){return"undefined";}
var a=[];for(var i=0;i<s.length;i++){var c=s.charAt(i);var b=c.charCodeAt(0);if(b>=0x160&&b<256){a[a.length]=("&#"+b+";");continue;}
switch(b){case 0x22:a[a.length]="&quot;";break;case 0x3C:a[a.length]="&lt;";break;case 0x3E:a[a.length]="&gt;";break;case 0x26:a[a.length]="&amp;";break;default:a[a.length]=c;break;}}
return a.join("");};var ParameterParser={};var Parameters=function(){this.parameters={};};Parameters.prototype.put=function(name,value){var list=this.parameters[name];if(list==null){list=[value];this.parameters[name]=list;}
else{list[list.length]=value;}};Parameters.prototype.setParameter=function(name){this.parameters[name]=[value];};Parameters.prototype.getParameter=function(name){var values=this.parameters[name];if(values==null||values.length<1){return null;}
return values[0];};Parameters.prototype.getParameterValues=function(name){return this.parameters[name];};Parameters.prototype.getObject=function(name){var object={};for(var name in this.parameters){var values=this.parameters[name];if(values==null||values.length<1){continue;}
if(values.length==1){object[name]=values[0];}
else{object[name]=values;}}
return object;};ParameterParser.parse=function(query){var params=new Parameters();if(query==null||query==""){return params;}
var name=[];var value=[];var i=query.indexOf("?");if(i>-1){query=query.substring(i+1);}
for(var i=0,length=query.length;i<length;i++){var c=query.charAt(i);if(c=="?"||c=="&"){continue;}
else if(c=="#"){break;}
else if(c=="="){for(i++;i<length;i++){c=query.charAt(i);if(c=="?"||c=="&"||c=="#"){if(c=="#"){i--;}
break;}
else{value[value.length]=c;}}
if(name.length>0){params.put(name.join(""),decodeURIComponent(value.join("")));}
name=[];value=[];}
else{name[name.length]=c;}}
return params;};var FileType={};FileType.getName=function(path){if(path!=null&&path.length>0){var c=null;var i=path.length-1;for(;i>-1;i--){c=path.charAt(i);if(c=="/"||c=="\\"||c==":"){break;}}
return path.substring(i+1);}
return"";};FileType.getExtension=function(path){if(path!=null&&path.length>0){var c=null;var i=path.length-1;for(;i>-1;i--){c=path.charAt(i);if(c=="."){return path.substring(i+1);}
else if(c=="/"||c=="\\"||c==":"){return"";}}}
return"";};FileType.getType=function(path){return FileType.getExtension(path).toLowerCase();};var ACEditor=function(id){this.id=id;};ACEditor.prototype.getId=function(){return this.id;};ACEditor.prototype.getTabId=function(){if(this.tabId==null){var hash=window.location.hash;if(hash.length>0&&hash.charAt(0)=="#"){hash=hash.substring(1);}
var parameters=ParameterParser.parse(hash);this.tabId=parameters.getParameter("tabId");}
return this.tabId;};ACEditor.prototype.getMode=function(path){var modelist=ace.require("ace/ext/modelist");return modelist.getModeForPath(path).mode;};ACEditor.prototype.getConfig=function(){if(window==window.parent){return{};}
if((window.parent.EditPlus)!="undefined"){return window.parent.EditPlus.getConfig();}
return{};};ACEditor.prototype.setConfig=function(name,value){if(window==window.parent){return{};}
if((window.parent.EditPlus)!="undefined"){window.parent.EditPlus.setConfig(name,value);}};ACEditor.prototype.extend=function(source,target){if(target==null){target={};}
for(var name in source){target[name]=source[name];}
return target;};ACEditor.prototype.create=function(){var e=document.getElementById(this.id);if(e==null){return;}
var file=this.getFile();var name=FileType.getName(file.path);var mode=this.getMode(file.path);var config=this.getConfig();var editor=ace.edit(this.id);var self=this;editor.session.setMode(mode);editor.setFontSize(config.fontSize);editor.setTheme("ace/theme/"+config.theme);editor.setOption("showGutter",config.showGutter);editor.setOption("showInvisibles",config.showInvisibles);editor.setOption("wrap",config.wrap);editor.setOption("enableBasicAutocompletion",config.enableBasicAutocompletion);editor.setOption("enableSnippets",config.enableSnippets);editor.setOption("enableLiveAutocompletion",config.enableLiveAutocompletion);editor.$blockScrolling="Infinity";var changeTimer=null;var StatusBar=ace.require("ace/ext/statusbar").StatusBar;new StatusBar(editor,document.getElementById("status-inf"));editor.session.on("change",function(delta){if(changeTimer!=null){clearTimeout(changeTimer);}
if(editor.session==null){return;}
var undoManager=editor.session.getUndoManager();changeTimer=setTimeout(function(){if(undoManager.hasUndo()){self.setChangeStatus(1);self.setTitle("* "+name);}
else{self.setChangeStatus(0);self.setTitle(name);}},200);});editor.commands.addCommand({name:"saveFile",bindKey:{win:"Ctrl-s",mac:"Command-s"},exec:function(editor){self.save();return true;}});this.load();};ACEditor.prototype.setTitle=function(title){if(window==window.parent||typeof(window.parent.EditPlus)=="undefined"){document.title=title;return;}
var tabId=this.getTabId();window.parent.EditPlus.setTitle(tabId,title);};ACEditor.prototype.getURL=function(action,host,workspace,path,charset){var params=[];params[params.length]="?action="+action;params[params.length]="host="+encodeURIComponent(host);params[params.length]="workspace="+encodeURIComponent(workspace);params[params.length]="path="+encodeURIComponent(path);if(charset!=null){params[params.length]="charset="+encodeURIComponent(charset);}
return params.join("&");};ACEditor.prototype.getEditor=function(){var e=document.getElementById(this.id);if(e!=null&&e.env!=null){return e.env.editor;}
return null;};ACEditor.prototype.setFile=function(file){if(this.file==null){this.file={};}
this.extend(file,this.file);};ACEditor.prototype.getFile=function(){if(this.file==null){var file=this.file={};var e=document.getElementById(this.id);if(e!=null){file.host=e.getAttribute("data-host");file.workspace=e.getAttribute("data-workspace");file.path=e.getAttribute("data-path");file.charset=e.getAttribute("data-charset");}}
return this.file;};ACEditor.prototype.setRange=function(range){if(this.range==null){this.range={};}
if(range.size>0){range.count=(range.end-range.start+1);}
else{range.count=0;}
this.extend(range,this.range);};ACEditor.prototype.getRange=function(range){return this.range;};ACEditor.prototype.getUndoManager=function(){var editor=this.getEditor();if(editor!=null){return editor.session.getUndoManager();}
return null;};ACEditor.prototype.setChangeStatus=function(status){if(status!=1){this.changeStatus=0;}
else{this.changeStatus=1;}};ACEditor.prototype.hasChange=function(){return(this.changeStatus==1);};ACEditor.prototype.setOption=function(name,value){var editor=this.getEditor();if(editor!=null){editor.setOption(name,value);}};ACEditor.prototype.setFontSize=function(fontSize){var editor=this.getEditor();if(editor!=null){editor.setFontSize(fontSize);}};ACEditor.prototype.setTheme=function(theme){var editor=this.getEditor();if(editor!=null){editor.setTheme(theme);}};ACEditor.prototype.setMode=function(mode){var editor=this.getEditor();if(editor!=null){editor.session.setMode("ace/mode/"+mode);}};ACEditor.prototype.getValue=function(){var editor=this.getEditor();if(editor!=null){return editor.getValue();}
return null;};ACEditor.prototype.resize=function(){var editor=this.getEditor();if(editor!=null){editor.resize();}};ACEditor.prototype.reload=function(){this.load();};ACEditor.prototype.load=function(){var id=this.id;var e=document.getElementById(id);if(e==null){alert("System Error, Please try again.");return;}
var file=this.getFile();var name=FileType.getName(file.path);var url=this.getURL("finder.getText",file.host,file.workspace,file.path);var editor=this.getEditor(id);var self=this;if(this.status==1){return false;}
else{this.status=1;}
console.log("load: "+file.path);jQuery.ajax({"type":"post","url":url,"dataType":"text","error":function(req,status,error){self.status=0;self.error(500,"System Error.");},"success":function(text,status,xhr){self.status=0;var response=self.getResponse(xhr);var range=response.value;if(response.status!="200"){self.error(response.status,response.message);return;}
editor.session.getUndoManager().reset();editor.setValue(text,-1);editor.setReadOnly(false);editor.session.setUndoManager(new ace.UndoManager());self.setRange(response.value);self.setChangeStatus(0);self.setTitle(name);if(range.count<range.size){alert("当前文件较大，将以只读模式显示部分数据！");editor.setReadOnly(true);}}});};ACEditor.prototype.save=function(){var id=this.id;var e=document.getElementById(id);if(e==null){alert("System Error, Please try again.");return false;}
if(this.range.start>0||this.range.count<this.range.size){alert("文件内容过长，不允许在线编辑！");return false;}
if(this.status==1){return false;}
else{this.status=1;}
var self=this;var file=this.getFile();var range=this.getRange();var content=this.getValue();var params=this.extend(this.range);var url=this.getURL("finder.save",file.host,file.workspace,file.path,file.charset)+"&"+jQuery.param(params,true);console.log("save: "+url);jQuery.ajax({"type":"post","url":url,"dataType":"json","data":"content="+encodeURIComponent(content),"error":function(req,status,error){self.status=0;alert("Save Failed.");},"success":function(response){self.status=0;if(response.status!=200){alert(response.message);return;}
self.setChangeStatus(0);self.setRange(response.value);self.setTitle(FileType.getName(file.path));}});};ACEditor.prototype.check=function(){var timeMillis=new Date().getTime();if(this.range==null){return;}
if(this.lastCheckTime!=null&&(timeMillis-this.lastCheckTime)<5000){return;}
var self=this;var file=this.getFile();var range=this.getRange();var url=this.getURL("finder.getFile",file.host,file.workspace,file.path);console.log("check: "+file.path);jQuery.ajax({"type":"post","url":url+"&size=2097152","dataType":"json","error":function(req,status,error){self.lastCheckTime=timeMillis;},"success":function(response){self.lastCheckTime=timeMillis;if(response.status!=200){return;}
var current=response.value;if(current.modified!=range.modified||current.size!=range.size){if(window.confirm("文件内容已经被外部修改，是否重新加载？")){self.reload();}}}});};ACEditor.prototype.getResponse=function(xhr){var response={"status":"500","message":"System Error."};var status=xhr.getResponseHeader("Finder-Status");var message=xhr.getResponseHeader("Finder-Message");var range=(xhr.getResponseHeader("Finder-Range")||"");var modified=xhr.getResponseHeader("Finder-Modified");if(message!=null){message=decodeURIComponent(message).replace(new RegExp("\\+","g")," ");}
if(status==null){return response;}
response.status=status;response.message=message;if(status!="200"){return response;}
if(range==null){response.status="500";response.message="Bad Response.";return response;}
var result=response.value={"start":0,"end":0,"size":0,"modified":0};try{var i=range.lastIndexOf("/");var size=parseInt(range.substring(i+1));var range=range.substring(0,i);var k=range.indexOf("-");var start=range.substring(0,k);var end=range.substring(k+1);result.start=parseInt(start);result.end=parseInt(end);result.size=parseInt(size);result.modified=parseInt(modified);if(result.size>0){result.count=(result.end-result.start+1);}
else{result.count=0;}
return response;}
catch(e){response.status="500";response.message="Bad Response.";return response;}};ACEditor.prototype.error=function(status,message){var editor=this.getEditor(this.id);if(editor!=null){editor.setReadOnly(true);}
alert(status+": "+message);};ACEditor.prototype.focus=function(){try{window.focus();}
catch(e){}
var editor=this.getEditor();if(editor!=null){editor.focus();}};ACEditor.prototype.undo=function(){var editor=this.getEditor();if(editor!=null){editor.undo();}};ACEditor.prototype.redo=function(){var editor=this.getEditor();if(editor!=null){editor.redo();}};ACEditor.prototype.destroy=function(){var editor=this.getEditor();if(editor!=null){editor.destroy();}};ACEditor.prototype.exec=function(name,args){var editor=this.getEditor();if(editor!=null){editor.execCommand(name,args);}};var instance=new ACEditor("editor");jQuery(function(){ace.require("ace/ext/language_tools");instance.create();});jQuery(function(){jQuery(window).resize(function(){var offset=document.getElementById("editor").offsetTop;var clientHeight=document.documentElement.clientHeight;document.getElementById("editor").style.height=(clientHeight-offset-20)+"px";instance.resize();});jQuery(window).resize();});window.ACEditor=instance;})();