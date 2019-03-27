// (c)2018 www.finderweb.net

(function(){if(typeof(com)!="undefined"&&typeof(com.skin)!="undefined"){return;}
var com={skin:{framework:{}}};var Class=com.skin.framework.Class={};Class.getClassId=function(){if(this.id==null){this.id=0;}
this.id++;return"class_"+this.id;};Class.create=function(parent,constructor,fields){var clazz=null;if(parent!=null){if(constructor!=null){clazz=function(){parent.apply(this,arguments);constructor.apply(this,arguments);};}
else{clazz=function(){parent.apply(this,arguments);};}
for(var property in parent.prototype){clazz.prototype[property]=parent.prototype[property];}
clazz.prototype["toString"]=parent.prototype["toString"];clazz.$super=parent.prototype;}
else{if(constructor!=null){clazz=function(){constructor.apply(this,arguments);};}
else{clazz=function(){};}
clazz.$super={};}
clazz.parent=parent;clazz.classId=this.getClassId();clazz.prototype.constructor=clazz;if(fields!=null&&fields.length>0){this.let(clazz,fields);}
return clazz;};Class.$super=function(instance,prototype){var object={};for(var i in prototype){if(typeof(prototype[i])=="function"){object[i]=function(){prototype[i].apply(instance,arguments);};}}
return object;};Class.getInstance=function(parent,constructor){return new(Class.create(parent,constructor))();};Class.copy=function(source,target){if(target==null){target={};}
for(var property in source){target[property]=source[property];}
return target;};Class.extend=function(child,parent){return this.copy(parent,child);};Class.let=function(f,fields){var p=f.prototype;for(var i=0,length=fields.length;i<length;i++){var name=fields[i];var method=name.charAt(0).toUpperCase()+name.substring(1);p["set"+method]=new Function(name,"this."+name+" = "+name+";");p["get"+method]=new Function("return this."+name+";");}};var ID={};ID.next=function(){if(this.seed==null){this.seed=0;}
this.seed++;return this.seed;};var DateUtil={};DateUtil.format=function(date){if(date==null){date=new Date();}
if(typeof(date)=="number"){var temp=new Date();temp.setTime(date);date=temp;}
var y=date.getFullYear();var M=date.getMonth()+1;var d=date.getDate();var h=date.getHours();var m=date.getMinutes();var a=[];a[a.length]=y;a[a.length]="-";if(M<10){a[a.length]="0";}
a[a.length]=M.toString();a[a.length]="-";if(d<10){a[a.length]="0";}
a[a.length]=d.toString();a[a.length]=" ";if(h<10){a[a.length]="0";}
a[a.length]=h.toString();a[a.length]=":";if(m<10){a[a.length]="0";}
a[a.length]=m.toString();return a.join("");};var StringUtil={};StringUtil.trim=function(s){return(s!=null?new String(s).replace(/(^\s*)|(\s*$)/g,""):"");};StringUtil.isBlank=function(text){if(text==null||text==undefined){return true;}
return(this.trim(text).length<=0);};StringUtil.startsWith=function(source,search){if(source.length>=search.length){return(source.substring(0,search.length)==search);}
return false;};StringUtil.endsWith=function(source,search){if(source.length>=search.length){return(source.substring(source.length-search.length,source.length)==search);}
return false;};StringUtil.contains=function(text,value){if(text==null||value==null){return false;}
var array=text;if(typeof(text)=="string"){if(text=="*"){return true;}
array=text.split(",");}
for(var i=0;i<array.length;i++){if(array[i]!=null){array[i]=StringUtil.trim(array[i]);if(array[i].length>0&&array[i]==value){return true;}}}
return false;};StringUtil.replace=function(source,search,replacement){if(source==null){return"";}
if(search==null||search.length<1){return source;}
if(replacement==null){replacement="";}
var s=0;var e=0;var d=search.length;var buffer=[];while(true){e=source.indexOf(search,s);if(e==-1){buffer[buffer.length]=source.substring(s);break;}
buffer[buffer.length]=source.substring(s,e);buffer[buffer.length]=replacement;s=e+d;}
return buffer.join("");};var StyleUtil={};StyleUtil.getRule=function(selector){var styleSheets=document.styleSheets;for(var i=0;i<styleSheets.length;i++){var styleSheet=styleSheets[i];var rules=styleSheet.rules?styleSheet.rules:styleSheet.cssRules;for(var j=0;j<rules.length;j++){var rule=rules[j];if(rule.selectorText.toLowerCase()==selector){return rule;}}}
return null;};StyleUtil.setStyleValue=function(selector,name,value){var rule=StyleUtil.getRule(selector);if(rule!=null){rule.style[name]=value;}};StyleUtil.getDocumentScrollTop=function(){return(document.documentElement.scrollTop||window.pageYOffset||document.body.scrollTop);};StyleUtil.getDocumentScrollLeft=function(){return(document.documentElement.scrollLeft||window.pageXOffset||document.body.scrollLeft);};var HtmlUtil={};HtmlUtil.replacement={"<":"&lt;",">":"&gt;","\"":"&quot;","\u00ae":"&reg;","\u00a9":"&copy;"};HtmlUtil.replace=function(e){return com.skin.util.Html.replacement[e];};HtmlUtil.encode=function(source,crlf){if(source==null){return"";}
if(crlf==null){crlf="";}
return source.replace(new RegExp("[<>\"\\u00ae\\u00a9]","g"),HtmlUtil.replace).replace(new RegExp("\\r?\\n","g"),crlf);};var ResourceLoader={};ResourceLoader.css=function(){var doc,url;if(arguments.length==0){return;}
else if(arguments.length==1){doc=document;url=arguments[0];}
else{doc=arguments[0];url=arguments[1];}
var scripts=doc.getElementsByTagName("link");for(var i=0,length=scripts.length;i<length;i++){var href=scripts[i].href;if(href!=null&&href!=undefined&&href.length>0){if(href.indexOf(url)>-1){return;}}}
var e=doc.createElement("link");e.rel="stylesheet";e.type="text/css";e.href=url;(doc.getElementsByTagName("head"))[0].appendChild(e);};ResourceLoader.script=function(){var doc,url;if(arguments.length==0){return;}
else if(arguments.length==1){doc=document;url=arguments[0];}
else{doc=arguments[0];url=arguments[1];}
var scripts=doc.getElementsByTagName("script");for(var i=0,length=scripts.length;i<length;i++){var src=scripts[i].src;if(src!=null&&src!=undefined&&src.length>0){if(src.indexOf(url)>-1){return;}}}
var e=doc.createElement("script");e.type="text/javascript";e.src=url;(doc.getElementsByTagName("head"))[0].appendChild(e);};var KeyCode={F1:112,F2:113,F3:114,F4:115,F5:116,F6:117,F7:118,F8:119,F9:120,F10:121,F11:122,F12:123,END:35,HOME:36,INSERT:45,DELETE:46,BACKSPACE:8,TAB:9,ENTER:13,SHIFT:16,CTRL:17,ALT:18,CAPSLOCK:20,ESC:27,BLANKSPACE:32,LEFT:37,UP:38,RIGHT:39,DOWN:40};KeyCode.isLetter=function(keyCode){return(keyCode>=65&&keyCode<=90);};KeyCode.isLetter=function(keyCode){return(keyCode>=48&&keyCode<=57);};var ListenerId={};ListenerId.next=function(){if(this.id==null){this.id=1;}
return this.id++;};var Listener=function(context){this.context=context;};Listener.prototype.dispatch=function(name,event){var handler=this[name];if(handler!=null){return handler.apply(this,[event]);}
else{return true;}};Listener.prototype.paste=function(event){return true;};Listener.prototype.keydown=function(event){return true;};Listener.prototype.keyup=function(event){return true;};Listener.prototype.mouseover=function(event){return true;};Listener.prototype.mousedown=function(event){return true;};Listener.prototype.mouseup=function(event){return true;};Listener.prototype.dblclick=function(event){return true;};Listener.prototype.click=function(event){return true;};Listener.prototype.dblclick=function(event){return true;};var Shortcut=Class.create(Listener,function(){this.listeners=[];});Shortcut.prototype.keydown=function(event){var src=(event.srcElement||event.target);var nodeName=src.nodeName.toLowerCase();if(nodeName=="input"||nodeName=="textarea"||nodeName=="select"){return true;}
var listeners=this.listeners;for(var i=0;i<listeners.length;i++){var listener=listeners[i];var shortcut=listener.shortcut;if(this.match(shortcut,event)==true){return listener.handler.apply(this.context,[event]);}}
return true;};Shortcut.prototype.match=function(shortcut,event){var a=shortcut.split("+");var keyCode=(event.keyCode||event.which);if(a.length<1){return false;}
for(var i=0;i<a.length;i++){var name=StringUtil.trim(a[i]);if(name.length<1){continue;}
if(name=="CTRL"){if(event.ctrlKey==true){continue;}
else{return false;}}
if(name=="SHIFT"){if(event.shiftKey==true){continue;}
else{return false;}}
if(name=="ALT"){if(event.altKey==true){continue;}
else{return false;}}
if(name.length>1){if(keyCode!=KeyCode[name]){return false;}}
else{if(keyCode!=name.charCodeAt(0)){return false;}}}
return true;};Shortcut.prototype.addListener=function(shortcut,handler){var a=shortcut.split("|");for(var i=0;i<a.length;i++){var shortcut=StringUtil.trim(a[i]);if(shortcut.length>0){this.listeners[this.listeners.length]={"shortcut":shortcut,"handler":handler};}}};var ArrayList=function(){this.elements=[];};ArrayList.prototype.add=function(e){this.elements[this.elements.length]=e;};ArrayList.prototype.contains=function(e){var elements=this.elements;for(var i=0;i<elements.length;i++){if(elements[i]==e){return true;}}
return false;};ArrayList.prototype.remove=function(e){var b=false;var list=[];var elements=this.elements;for(var i=0;i<elements.length;i++){if(elements[i]!=e){list[list.length]=elements[i];}
else{b=true;}}
this.elements=list;return b;};ArrayList.prototype.get=function(i){if(i>=0&&i<this.elements.length){return this.elements[i];}
else{return null;}};ArrayList.prototype.first=function(i){return this.get(0);};ArrayList.prototype.last=function(i){var size=this.size();return this.get(size-1);};ArrayList.prototype.size=function(i){return this.elements.length;};ArrayList.prototype.list=function(){var list=[];var elements=this.elements;for(var i=0;i<elements.length;i++){list[list.length]=elements[i];}
return list;};ArrayList.prototype.each=function(handler){var list=[];var elements=this.elements;for(var i=0;i<elements.length;i++){var flag=handler(elements[i]);if(flag==false){break;}}};var LinkedList=function(){this.head=null;this.tail=null;};LinkedList.prototype.add=function(e){var node=new Node(null,null,e);if(this.head==null){this.head=node;this.tail=node;}
else{node.prev=this.tail;this.tail.next=node;this.tail=node;}};LinkedList.prototype.contains=function(e){var node=this.head;while(node!=null){if(node.item==e){return true;}
else{node=node.next;}}
return false;};LinkedList.prototype.remove=function(e){var node=this.head;while(node!=null){if(node.item==e){var prev=node.prev;var next=node.next;if(prev==null){this.head=next;if(next!=null){next.prev=null;}}
else{prev.next=next;if(next!=null){next.prev=prev;}}
if(next==null){this.tail=prev;}
else{}
node.prev=null;node.next=null;node=next;}
else{node=node.next;}}
return false;};LinkedList.prototype.get=function(i){var index=0;var element=null;this.each(function(e){if(index==i){element=e;return false;}
index++;});return element;};LinkedList.prototype.first=function(i){if(this.head!=null){return this.head.item;}
else{return null;}};LinkedList.prototype.last=function(i){if(this.tail!=null){return this.tail.item;}
else{return null;}};LinkedList.prototype.size=function(){var length=0;var node=this.head;while(node!=null){node=node.next;length++;}
return length;};LinkedList.prototype.list=function(){var list=[];var node=this.head;while(node!=null){list[list.length]=node.item;node=node.next;}};LinkedList.prototype.each=function(handler){var list=[];var node=this.head;while(node!=null){var flag=handler(node.item);if(flag==false){break;}
node=node.next;}};var Node=function(prev,next,item){this.prev=prev;this.next=next;this.item=item;};Node.prototype.toString=function(){var buffer=[];buffer[buffer.length]=this.item;if(this.next!=null){buffer[buffer.length]=" -> "+this.next.toString();}
return buffer.join("");};var BindUtil={};BindUtil.bind=function(object,handler){return function(){return handler.apply(object,arguments);}};BindUtil.bindAsEventListener=function(object,handler){return function(event){var b=handler.call(object,(event||window.event));if(b==false){EventUtil.stop(event);}
return b;}};var EventUtil={};EventUtil.addEventListener=function(target,type,handler){if(target.addEventListener){target.addEventListener(type,handler,false);}
else if(target.attachEvent){target.attachEvent("on"+type,handler);}
else{target["on"+type]=handler;}};EventUtil.removeEventListener=function(target,type,handler){if(target.detachEvent){target.detachEvent("on"+type,handler);}
else if(target.removeEventListener){target.removeEventListener(type,handler,false);}
else{target["on"+type]=null;}};EventUtil.stop=function(event){if(event!=null){if(event.stopPropagation){event.stopPropagation();}
if(event.preventDefault){event.preventDefault();}
event.cancel=true;event.cancelBubble=true;event.returnValue=null;}
return false;};var I18N={};I18N.bundle={};I18N.format=function(pattern){var c;var buffer=[];var args=arguments;for(var i=0,length=pattern.length;i<length;i++){c=pattern.charAt(i);if(c=="\\"&&i<length-1){i=i+1;c=pattern.charAt(i);if(c=="{"||c=="}"){buffer[buffer.length]=c;continue;}
else{buffer[buffer.length]="\\";buffer[buffer.length]=c;continue;}}
else if(c=="{"){var k=pattern.indexOf("}",i+1);if(k>-1){var index=parseInt(pattern.substring(i+1,k));if(!isNaN(index)&&index>0&&index<args.length){buffer[buffer.length]=(args[index]||"");}
i=k;}
else{buffer[buffer.length]=pattern.substring(i+1);break;}}
else{buffer[buffer.length]=c;}}
return buffer.join("");};I18N.getLang=function(key){var value=this.bundle[key];if(value==null||value==undefined){return key;}
var args=[value].concat([].slice.call(arguments,1));return this.format.apply(this,args);};var Draggable=function(source,target){this.x=0;this.y=0;this.source=source;this.target=target;this.dragging=false;this.mask=document.getElementById("widget-draggable-mask");this.frame=document.getElementById("widget-draggable-frame1");this.stopHandler=BindUtil.bindAsEventListener(this,this.stop);this.moveHandler=BindUtil.bindAsEventListener(this,this.move);if(this.mask==null){this.mask=document.createElement("div");this.mask.id="widget-draggable-mask";this.mask.className="widget-draggable-mask";document.body.appendChild(this.mask);}
if(this.frame==null){this.frame=document.createElement("div");this.frame.id="widget-draggable-frame1";this.frame.className="widget-draggable-frame1";this.frame.innerHTML="<div class=\"widget-draggable-body1\"></div>";document.body.appendChild(this.frame);}
if(this.target!=null){this.target.style.position="absolute";}
if(this.source!=null){EventUtil.addEventListener(this.source,"mousedown",BindUtil.bindAsEventListener(this,this.start));}};Draggable.prototype.start=function(event){var src=(event.srcElement||event.target);var keyCode=(event.keyCode||event.which);if(keyCode!=1){return true;}
if(src.getAttribute("draggable")=="false"){return true;}
this.dragging=false;this.startX=event.clientX;this.startY=event.clientY;this.offsetY=event.clientY-this.target.offsetTop;this.offsetX=event.clientX-this.target.offsetLeft;this.frame.style.width=(this.target.offsetWidth-2)+"px";this.frame.style.height=(this.target.offsetHeight-2)+"px";this.frame.style.display="none";EventUtil.addEventListener(document,"mouseup",this.stopHandler);EventUtil.addEventListener(document,"mousemove",this.moveHandler);return true;};Draggable.prototype.move=function(event){var x=event.clientX-this.offsetX;var y=event.clientY-this.offsetY;if(y<0){return false;}
if(this.dragging==false){if(Math.abs(event.clientX-this.startX)>2||Math.abs(event.clientY-this.startY)>2){this.dragging=true;}
else{return false;}}
var zIndex=parseInt(this.target.style.zIndex);this.mask.style.zIndex=zIndex+8;this.mask.style.display="block";this.frame.style.top=y+"px";this.frame.style.left=x+"px";this.frame.style.zIndex=zIndex+10;this.frame.style.display="block";return false;};Draggable.prototype.stop=function(event){var y=this.frame.offsetTop;var x=this.frame.offsetLeft;if(y<0){y=0;}
EventUtil.removeEventListener(document,"mouseup",this.stopHandler);EventUtil.removeEventListener(document,"mousemove",this.moveHandler);this.frame.style.zIndex=-1;this.frame.style.display="none";this.mask.style.display="none";if(this.dragging!=true){return false;}
this.dragging=false;this.target.style.marginTop="0px";this.target.style.marginLeft="0px";this.target.style.top=y+"px";this.target.style.left=x+"px";return false;};Draggable.registe=function(source,target){var e1=null;var e2=null;if(typeof(source)=="string"){e1=document.getElementById(source);}
else{e1=source;}
if(typeof(target)=="string"){e2=document.getElementById(target);}
else{e2=target;}
if(e1!=null&&e2!=null){new Draggable(e1,e2);}};var Draggable2=function(source){this.clicked=false;this.dragging=false;this.frame=document.getElementById("widget-draggable-frame2");this.stopHandler=BindUtil.bindAsEventListener(this,this.stop);this.moveHandler=BindUtil.bindAsEventListener(this,this.move);if(this.frame==null){this.frame=document.createElement("div");this.frame.id="widget-draggable-frame2";this.frame.className="widget-draggable-frame2";document.body.appendChild(this.frame);}
if(source!=null){EventUtil.addEventListener(source,"mousedown",BindUtil.bindAsEventListener(this,this.start));}};Draggable2.prototype.start=function(event){var src=(event.srcElement||event.target);var keyCode=(event.keyCode||event.which);if(keyCode!=1){return true;}
this.clicked=false;this.startX=event.clientX;this.startY=event.clientY;if(this.onstart!=null&&this.onstart(event)==false){return true;}
EventUtil.addEventListener(document,"mouseup",this.stopHandler);EventUtil.addEventListener(document,"mousemove",this.moveHandler);return true;};Draggable2.prototype.move=function(event){var x=event.clientX;var y=event.clientY;if(y<0){return false;}
if(this.dragging==false){if(Math.abs(this.startX-x)>2||Math.abs(this.startY-y)>2){this.dragging=true;}}
if(this.dragging==false){return false;}
if(this.onmove!=null){this.onmove(event)}
return false;};Draggable2.prototype.stop=function(event){var y=this.frame.offsetTop;var x=this.frame.offsetLeft;if(y<0){y=0;}
EventUtil.removeEventListener(document,"mouseup",this.stopHandler);EventUtil.removeEventListener(document,"mousemove",this.moveHandler);this.frame.style.display="none";this.frame.removeAttribute("data-text");if(this.onstop!=null){this.onstop(event);}
if(this.dragging==true){return(this.dragging=false);}
else{return true;}};Draggable2.prototype.setText=function(text){this.frame.setAttribute("data-text",text);};Draggable2.prototype.getText=function(){return this.frame.getAttribute("data-text");};Draggable2.registe=function(source){var e1=null;var e2=null;if(typeof(source)=="string"){e1=document.getElementById(source);}
else{e1=source;}
if(e1!=null){new Draggable2(e1);}};var DraggableListener2={};DraggableListener2.listeners=[];DraggableListener2.listen=function(fn){this.listeners[this.listeners.length]=fn;};DraggableListener2.trigger=function(){var listeners=this.listeners;for(var i=0;i<listeners.length;i++){var b=(listeners[i])();if(b==false){return;}}};var Selectable=function(src){this.x=0;this.y=0;this.width=0;this.height=0;this.clicked=false;this.dragging=false;this.container=src;this.timer=null;if(src!=null){this.bind(src);}};Selectable.prototype.bind=function(src){if(src==null){return;}
var id=ID.next();var position=jQuery(src).css("position");if(position!="absolute"&&position!="relative"){src.style.position="relative";}
this.mask=document.getElementById("widget-draggable-mask"+id);this.frame=document.getElementById("widget-selectable-frame"+id);this.stopHandler=BindUtil.bindAsEventListener(this,this.stop);this.moveHandler=BindUtil.bindAsEventListener(this,this.move);this.timer=null;if(this.mask==null){this.mask=document.createElement("div");this.mask.id="widget-draggable-mask"+id;this.mask.className="widget-draggable-mask";src.appendChild(this.mask);}
if(this.frame==null){this.frame=document.createElement("div");this.frame.id="widget-selectable-frame"+id;this.frame.className="widget-selectable-frame";src.appendChild(this.frame);}
EventUtil.addEventListener(src,"mousedown",BindUtil.bindAsEventListener(this,this.start));};Selectable.prototype.start=function(event){var src=(event.srcElement||event.target);var keyCode=(event.keyCode||event.which);if(this.timer!=null){clearTimeout(this.timer);}
if(keyCode!=1){return true;}
if(src.getAttribute("draggable")=="false"){return true;}
var self=this;var offset=jQuery(this.container).offset();this.startX=event.clientX-offset.left+this.container.scrollLeft;this.startY=event.clientY-offset.top+this.container.scrollTop;if(this.onstart!=null&&this.onstart(event)==false){return true;}
EventUtil.addEventListener(document,"mouseup",this.stopHandler);EventUtil.addEventListener(document,"mousemove",this.moveHandler);return true;};Selectable.prototype.move=function(event){if(this.timer!=null){clearTimeout(this.timer);}
var b=false;var offset=jQuery(this.container).offset();var x=event.clientX-offset.left+this.container.scrollLeft;var y=event.clientY-offset.top+this.container.scrollTop;if(this.dragging==false){if(Math.abs(this.startX-x)>2||Math.abs(this.startY-y)>2){this.dragging=true;}
else{return false;}}
var clientWidth=this.container.clientWidth;var clientHeight=this.container.clientHeight;var scrollLeft=this.container.scrollLeft;var scrollTop=this.container.scrollTop;if(x<=scrollLeft){this.container.scrollLeft=Math.max(scrollLeft-20,0);x=scrollLeft;b=(x>0);}
if(x>=(clientWidth+scrollLeft)){this.container.scrollLeft=scrollLeft+20;if(x>=(this.container.scrollWidth-2)){x=this.container.scrollWidth-2;b=false;}
else{b=true;}}
if(y<=scrollTop){this.container.scrollTop=Math.max(scrollTop-30,0);y=Math.max(scrollTop-30,0);b=(y>0);}
if(y>=(clientHeight+scrollTop)){this.container.scrollTop=scrollTop+30;y=Math.min(clientHeight+scrollTop+30,y+30);if(y>=(this.container.scrollHeight-2)){y=this.container.scrollHeight-2;b=false;}
else{b=true;}}
this.x=x;this.y=y;this.width=this.x-this.startX;this.height=this.y-this.startY;if(this.width>0){this.x=this.startX;}
else{this.width=Math.abs(this.width);}
if(this.height>0){this.y=this.startY;}
else{this.height=Math.abs(this.height);}
this.mask.style.height=this.container.scrollHeight+"px";this.mask.style.zIndex=10000;this.mask.style.display="block";this.frame.style.top=this.y+"px";this.frame.style.left=this.x+"px";this.frame.style.width=this.width+"px";this.frame.style.height=this.height+"px";this.frame.style.zIndex=10001;this.frame.style.display="block";if(this.onmove!=null){this.onmove(event)}
if(b==true){var self=this;this.timer=setTimeout(function(){self.move(event);},60);}
return false;};Selectable.prototype.stop=function(event){if(this.timer!=null){clearTimeout(this.timer);}
var y=this.frame.offsetTop;var x=this.frame.offsetLeft;if(y<0){y=0;}
EventUtil.removeEventListener(document,"mouseup",this.stopHandler);EventUtil.removeEventListener(document,"mousemove",this.moveHandler);this.mask.style.display="none";this.frame.style.display="none";this.frame.removeAttribute("data-text");if(this.onstop!=null){this.onstop(event);}
if(this.dragging==true){return(this.dragging=false);}
else{return true;}};var TaskManager={};TaskManager.id="widget-task-bar";TaskManager.tasks=new ArrayList();TaskManager.getById=function(id){var dialog=null;this.tasks.each(function(e){if(e.getId()==id){dialog=e;return false;}
else{return true;}});return dialog;};TaskManager.add=function(dialog){var dialogId=dialog.getId();if(this.getById(dialogId)==null){this.tasks.remove(dialog);this.tasks.add(dialog);this.show();}};TaskManager.remove=function(dialog){var task=this.getById(dialog.getId());this.tasks.remove(task);jQuery("#"+this.id+" ul li[dialogId="+dialog.getId()+"]").remove();};TaskManager.show=function(){var c=document.getElementById(this.id);if(c==null){c=document.createElement("div");c.id=this.id;c.className="widget-task-bar";c.setAttribute("contextmenu","false");document.body.appendChild(c);}
var buffer=[];var tasks=this.tasks;buffer[buffer.length]="<ul>";for(var i=0,size=tasks.size();i<size;i++){var dialog=tasks.get(i);if(dialog.parent==null){buffer[buffer.length]="<li dialogId=\""+dialog.getId()+"\" title=\""+dialog.title+"\"onclick=\"TaskManager.toggle(this)\">";if(dialog.icon!=null){buffer[buffer.length]="<span class=\"icon\"><img src=\""+dialog.icon+"\"></span>";}
buffer[buffer.length]="<span class=\"title\">"+dialog.title+"</span>";buffer[buffer.length]="</li>";}}
buffer[buffer.length]="</ul>";c.innerHTML=buffer.join("");};TaskManager.setTitle=function(dialog){var dialogId=dialog.getId();if(dialog.icon!=null){jQuery("#"+this.id+" ul li[dialogId="+dialogId+"]").html("<img class=\"task-icon\" src=\""+dialog.icon+"\">"+dialog.title);}
else{jQuery("#"+this.id+" ul li[dialogId="+dialogId+"]").html(dialog.title);}};TaskManager.toggle=function(src){var id=src.getAttribute("dialogId");var dialog=this.getById(id);if(dialog!=null){dialog.toggle();return;}
var e=document.getElementById(id);if(e!=null){if(e.style.display=="none"){e.style.display="block";}
else{e.style.display="none";}}};TaskManager.getContainer=function(){return document.getElementById(this.id);};var DialogManager={};DialogManager.zIndex=1000;DialogManager.active=null;DialogManager.dialogs=new ArrayList();DialogManager.setActive=function(dialog,b){if(b==true){this.push(dialog);}
else{this.pop(dialog);}};DialogManager.add=function(dialog){if(dialog.parent==null){var last=this.dialogs.last();if(last!=dialog){this.dialogs.remove(dialog);this.dialogs.add(dialog);}}};DialogManager.push=function(dialog){var last=this.dialogs.last();if(last!=dialog){this.dialogs.remove(dialog);this.dialogs.add(dialog);this.dialogs.each(function(e){if(e!=dialog){e.setActiveStyle(false);}});dialog.setActiveStyle(true);}};DialogManager.pop=function(dialog){this.dialogs.remove(dialog);dialog.setActiveStyle(false);var dialog=this.getActive();if(dialog!=null){this.push(dialog);}
return true;};DialogManager.remove=function(dialog){return this.dialogs.remove(dialog);};DialogManager.getActive=function(){return this.dialogs.last();};DialogManager.getZIndex=function(){this.zIndex=this.zIndex+5;return this.zIndex;};DialogManager.dispatch=function(name,event){var dialog=this.getActive();if(dialog!=null){return dialog.getListener().dispatch(name,event);}
else{return true;}};var Mask={"id":"_dialog_mask"};Mask.queue=[];Mask.show=function(id,zIndex){var e=document.getElementById(this.id);var cssText=["display: none;","position: absolute;","top: 0px; left: 0px;","width: 100%; height: 100%;","background: #000000;","opacity: 0.5; filter: alpha(opacity=50);"].join(" ");if(e!=null){e.style.cssText=cssText;}
else{e=document.createElement("div");e.id=this.id;e.className="dialog-mask";e.style.cssText=cssText;e.setAttribute("contextmenu","false");document.body.appendChild(e);}
if(e.childNodes.length<1){var html=["<iframe frameborder=\"0\" scrolling=\"no\""," style=\"position: relative; top: 0px; left: 0px;"," width: 100%; height: 100%;"," border: 0px solid #5183dc; background-color: transparent; z-index: 9991; overflow: hidden;\""," src=\"about:blank\"></iframe>"].join("");e.innerHTML=html;var frame=e.childNodes[0].contentWindow.document;frame.open("text/html");frame.write("<html><head><title>mask</title></head><body style=\"background-color: #000000;\"></body></html>");frame.close();}
e.style.zIndex=zIndex;e.style.display="block";this.resize();this.block(id);};Mask.block=function(id){var length=this.queue.length;for(var i=0;i<length;i++){if(this.queue[i]==id){return true;}}
for(var i=0;i<length;i++){if(this.queue[i]==""){this.queue[i]=id;return true;}}
this.queue[length]=id;return true;};Mask.resize=function(){var e=document.getElementById(this.id);if(e!=null){var width=document.documentElement.scrollWidth;var height=document.documentElement.scrollHeight;if(document.documentElement.scrollWidth<document.documentElement.clientWidth){width=document.documentElement.clientWidth;}
if(document.documentElement.scrollHeight<document.documentElement.clientHeight){height=document.documentElement.clientHeight;}
e.style.width="100%";e.style.height="100%";if(e.childNodes.length>0){var frame=e.childNodes[0];frame.style.width=width;frame.style.width=height;}}};Mask.close=function(id){var e=document.getElementById(this.id);for(var i=0;i<this.queue.length;i++){if(this.queue[i]==id){this.queue[i]="";}}
if(this.queue.join("").length<1){this.queue.length=0;if(e!=null){e.style.display="none";}
return false;}
return true;};var Dialog=com.skin.framework.Class.create(null,function(args){var options=(args||{});this.id=null;this.title=options.title;this.block=options.block;this.parent=options.parent;this.childs=new ArrayList();this.zIndex=DialogManager.getZIndex();if(options.container==null){this.id="_widget_component_"+ID.next();}
else{if(typeof(options.container)=="string"){this.id=options.container;}
else{if(options.container.id!=null){this.id=options.container.id;}
else{this.id="_widget_component_"+ID.next();options.container.id=this.id;}}}
if(options.parent!=null){options.parent.childs.add(this);}
var self=this;var container=this.getContainer();if(container!=null){if(container==document.body||container==window||container==document){throw{"name":"Error","message":"窗口容器不允许嵌套！"};}
EventUtil.addEventListener(container,"mousedown",function(event){DialogManager.push(self);});EventUtil.addEventListener(container,"contextmenu",function(event){DialogManager.push(self);});}
var content=this.getContent();if(content!=null){this.setContent(content);}
this.create();});Dialog.prototype.getId=function(title){return this.id;};Dialog.prototype.create=function(content){};Dialog.prototype.setIcon=function(icon){this.icon=icon;var container=jQuery(this.getContainer());container.find("div.title span.icon").html("<img src=\""+this.icon+"\"/>");};Dialog.prototype.setTitle=function(title){this.title=title;var container=jQuery(this.getContainer());container.find("div.title h4").html(title);};Dialog.prototype.open=function(arg){if(this.block==true){Mask.show(this.id);}
return this.show(arg);};Dialog.prototype.close=function(){TaskManager.remove(this);this.setVisiable(false,false);if(this.parent!=null){this.parent.childs.remove(this);}
this.childs.each(function(e){e.close();});};Dialog.prototype.exit=function(){this.destroy();};Dialog.prototype.destroy=function(){TaskManager.remove(this);DialogManager.pop(this);var c=this.getContainer();if(c!=null){c.parentNode.removeChild(c);}
if(this.parent!=null){this.parent.childs.remove(this);}
this.childs.each(function(e){e.destroy();});};Dialog.prototype.setContent=function(content){var c=this.getContainer();if(c!=null){if(typeof(content)=="string"){c.innerHTML=content;}
else{c.appendChild(content);}}};Dialog.prototype.getContent=function(){return null;};Dialog.prototype.setVisiable=function(visiable,active){var c=this.getContainer();if(c!=null){if(visiable==true){if(active==true){DialogManager.setActive(this,true);}
if(this.block==true){Mask.show(this.id,this.zIndex-1);}
c.style.display="block";}
else{DialogManager.setActive(this,false);if(this.block==true){Mask.close(this.id);}
c.style.display="none";}}};Dialog.prototype.focus=function(active){var c=this.getContainer();if(c!=null){try{c.focus();}
catch(e){}}};Dialog.prototype.setActive=function(active){DialogManager.setActive(this,active);};Dialog.prototype.setActiveStyle=function(active){var c=this.getContainer();if(c!=null){if(active==true){c.style.zIndex=DialogManager.getZIndex();}
else{c.style.zIndex=this.zIndex;}}};Dialog.prototype.show=function(arg){var c=this.getContainer();if(c!=null){c.style.display="block";var width=this.getWidth(c);var height=this.getHeight(c);var top=document.documentElement.scrollTop+document.body.scrollTop;var left=document.documentElement.scrollLeft+document.body.scrollLeft;var y=top+parseInt((document.documentElement.clientHeight-height)/2);var x=left+parseInt((document.documentElement.clientWidth-width)/2);c.style.top=(y>0?y:0)+"px";c.style.left=(x>0?x:0)+"px";this.setVisiable(true,true);}
return this;};Dialog.prototype.hide=function(){var c=this.getContainer();if(c!=null){c.style.display="none";}
return this;};Dialog.prototype.toggle=function(){var c=this.getContainer();if(c!=null){if(c.style.display!="none"){c.style.display="none";}
else{c.style.display="block";}}
return this;};Dialog.prototype.setListener=function(listener){this.listener=listener;};Dialog.prototype.getListener=function(){if(this.listener==null){this.listener=new Shortcut(this);}
return this.listener;};Dialog.prototype.getShortcut=function(){return this.getListener();};Dialog.prototype.addShortcut=function(key,handler){if(typeof(handler)=="string"){var command=handler;this.getListener().addListener(key,function(event){this.close();this.execute(command);return false;});}
else{this.getListener().addListener(key,handler);}};Dialog.prototype.getContainer=function(){var c=document.getElementById(this.id);if(c==null){c=document.createElement("div");c.id=this.id;c.className="dialog";c.setAttribute("contextmenu",false);document.body.appendChild(c);}
return c;};Dialog.prototype.getZIndex=function(){return this.zIndex;};Dialog.prototype.getStyle=function(e,name){if(e.style[name]){return e.style[name];}
else if(document.defaultView!=null&&document.defaultView.getComputedStyle!=null){var computedStyle=document.defaultView.getComputedStyle(e,null);if(computedStyle!=null){var property=name.replace(/([A-Z])/g,"-$1").toLowerCase();return computedStyle.getPropertyValue(property);}}
else if(e.currentStyle!=null){return e.currentStyle[name];}
return null;};Dialog.prototype.getWidth=function(e){var result=this.getStyle(e,"width");if(result!=null){result=parseInt(result.replace("px",""));}
return isNaN(result)?0:result;};Dialog.prototype.getHeight=function(e){var result=this.getStyle(e,"height");if(result!=null){result=parseInt(result.replace("px",""));}
return isNaN(result)?0:result;};var MessageDialog=Class.create(Dialog,function(){this.block=true;});MessageDialog.prototype.create=function(){var self=this;var buffer=[];buffer[buffer.length]="<div class=\"title\">";buffer[buffer.length]="    <span class=\"icon\"><img src=\"?action=res&path=/finder/images/viewtip.gif\"/></span>";buffer[buffer.length]="    <h4 class=\"alert\">Alert</h4>";buffer[buffer.length]="    <span class=\"close\" draggable=\"false\"></span>";buffer[buffer.length]="</div>";buffer[buffer.length]="<div class=\"body20\">";buffer[buffer.length]="    <div class=\"text\"></div>";buffer[buffer.length]="</div>";buffer[buffer.length]="<div class=\"button right\">";buffer[buffer.length]="    <button type=\"button\" class=\"button ensure\" href=\"javascript:void(0)\">Ok</button>";buffer[buffer.length]="</div>";this.setContent(buffer.join(""));var container=jQuery(this.getContainer());container.css("minWidth","400px");container.find("div.text").html("");container.find("div.button button.ensure").unbind();container.find("div.title span.close").unbind();container.find("div.button button.ensure").click(function(){self.close();self.destroy();if(self.ensure!=null){self.ensure();}
return false;});container.find("div.title span.close").click(function(){self.close();self.destroy();if(self.ensure!=null){self.ensure();}
return false;});this.addShortcut("ENTER",function(){self.close();self.destroy();if(self.ensure!=null){self.ensure(true);}
return false;});this.addShortcut("ESC",function(){self.close();self.destroy();if(self.ensure!=null){self.ensure();}
return false;});Draggable.registe(container.find("div.title").get(0),container.get(0));};MessageDialog.prototype.setTitle=function(title){var container=jQuery(this.getContainer());container.find("div.title h4.alert").html(title);};MessageDialog.prototype.setEnsureText=function(text){var container=jQuery(this.getContainer());container.find("div.button button.ensure").value=text;};MessageDialog.prototype.open=function(message){var container=jQuery(this.getContainer());container.find("div.text").html(message);return this.show();};var ConfirmDialog=Class.create(Dialog,function(){this.block=true;});ConfirmDialog.prototype.create=function(){var self=this;var buffer=[];buffer[buffer.length]="<div class=\"title\">";buffer[buffer.length]="    <span class=\"icon\"><img src=\"?action=res&path=/finder/images/viewtip.gif\"/></span>";buffer[buffer.length]="    <h4 class=\"alert\">Confirm</h4>";buffer[buffer.length]="    <span class=\"close\" draggable=\"false\"></span>";buffer[buffer.length]="</div>";buffer[buffer.length]="<div class=\"body20\">";buffer[buffer.length]="    <div class=\"text\"></div>";buffer[buffer.length]="</div>";buffer[buffer.length]="<div class=\"button right\">";buffer[buffer.length]="    <button type=\"button\" class=\"button ensure\" href=\"javascript:void(0)\">Ok</button>";buffer[buffer.length]="    <button type=\"button\" class=\"button cancel\" href=\"javascript:void(0)\">Cancel</button>";buffer[buffer.length]="</div>";this.setContent(buffer.join(""));var container=jQuery(this.getContainer());container.css("minWidth","400px");container.find("div.text").html("");container.find("div.button button.ensure").unbind();container.find("div.button button.cancel").unbind();container.find("div.title span.close").unbind();container.find("div.button button.ensure").click(function(){self.close();self.destroy();if(self.ensure!=null){self.ensure(true);}
return false;});container.find("div.button button.cancel").click(function(event){self.close();self.destroy();if(self.cancel!=null){self.cancel(false);}
return false;});container.find("div.title span.close").click(function(){self.close();self.destroy();if(self.cancel!=null){self.cancel(false);}
return false;});this.addShortcut("ENTER",function(){self.close();self.destroy();if(self.ensure!=null){self.ensure(true);}
return false;});this.addShortcut("ESC",function(){self.close();self.destroy();if(self.cancel!=null){self.cancel();}
return false;});Draggable.registe(container.find("div.title").get(0),container.get(0));};ConfirmDialog.prototype.setTitle=function(title){var container=jQuery(this.getContainer());container.find("div.title h4.alert").html(title);};ConfirmDialog.prototype.setEnsureText=function(text){var container=jQuery(this.getContainer());container.find("div.button button.ensure").value=text;};ConfirmDialog.prototype.setCancelText=function(text){var container=jQuery(this.getContainer());container.find("div.button button.cancel").value=text;};ConfirmDialog.prototype.open=function(message){var container=jQuery(this.getContainer());container.find("div.text").html(message);return this.show();};var FrameDialog=Class.create(Dialog,function(){this.block=true;});FrameDialog.prototype.create=function(){var buffer=[];buffer[buffer.length]="<div class=\"title\">";buffer[buffer.length]="<span class=\"icon\"><img src=\"?action=res&path=/finder/images/viewtip.gif\"/></span>";buffer[buffer.length]="<h4>&nbsp;</h4>";buffer[buffer.length]="<span class=\"close\" draggable=\"false\"></span>";buffer[buffer.length]="<span class=\"max\" draggable=\"false\"></span>";buffer[buffer.length]="<span class=\"min\" draggable=\"false\"></span>";buffer[buffer.length]="</div>";buffer[buffer.length]="<div class=\"body\">";buffer[buffer.length]="<iframe src=\"about:blank\" style=\"margin: 0px; padding: 0px; width: 100%; height: 200px;\" frameborder=\"0\" scrolling=\"auto\" marginwidth=\"0\" marginheight=\"0\"></iframe>";buffer[buffer.length]="</div>";this.setContent(buffer.join(""));var self=this;var container=jQuery(this.getContainer());container.css("minWidth","400px");if(this.icon!=null){container.find("div.title span.icon").html("<img src=\""+this.icon+"\"/>");}
if(this.title!=null){container.find("div.title h4").html(this.title);}
container.find("div.title").dblclick(function(){container.find("div.title span.max").click();});container.find("div.title span.min").click(function(){self.hide();TaskManager.add(self);});container.find("div.title span.max").click(function(){var status=container.attr("data-win");if(status!="1"){var width=container.width();var height=container.height();var winWidth=jQuery(window).width();var winHeight=jQuery(window).height();var position=container.position();container.attr("data-win","1");container.attr("data-top",position.top);container.attr("data-left",position.left);container.attr("data-width",width);container.attr("data-height",height);container.css({"top":"0px","left":"0px","width":"100%","height":"100%"});container.find("div.body iframe").css({"width":"100%","height":(winHeight-30)+"px"});}
else{var top=container.attr("data-top");var left=container.attr("data-left");var width=container.attr("data-width");var height=container.attr("data-height");container.attr("data-win","0");container.css({"top":top+"px","left":left+"px","width":width+"px","height":height+"px"});container.find("div.body iframe").css({"width":"100%","height":(parseInt(height)-30)+"px"});}});container.find("div.title span.close").unbind();container.find("div.title span.close").click(function(){self.close();self.destroy();return false;});this.addShortcut("ESC",function(){self.close();self.destroy();return false;});Draggable.registe(container.find("div.title").get(0),container.get(0));};FrameDialog.prototype.open=function(url,width,height){var container=jQuery(this.getContainer());container.css({"width":width+"px","height":height+"px"});container.find("div.body").css("font-size","0px");container.find("div.body").css("overflow","hidden");container.find("div.body iframe").css("height",(height-30)+"px");container.find("div.body iframe").attr("src",url);return this.show();};FrameDialog.prototype.getIFrame=function(url){var container=jQuery(this.getContainer());var iframe=container.find("div.body iframe");if(iframe.size()>0){return iframe.get(0);}
return null;};FrameDialog.prototype.setURL=function(url){var iframe=this.getIFrame();if(iframe!=null){iframe.src=url;}};FrameDialog.setTitle=function(id,title){jQuery("#"+id+" div.title h4").text(title);};var ContextMenu=Class.create(Dialog,null);ContextMenu.prototype.create=function(){var self=this;var container=this.getContainer();this.addShortcut("UP",function(event){self.scroll(false);return false;});this.addShortcut("DOWN",function(event){self.scroll(true);return false;});this.addShortcut("ENTER",function(event){var item=self.getSelected();self.close();self.execute(item.getAttribute("command"));return false;});jQuery(container).find("ul li.item").mouseover(function(){if(jQuery(this).hasClass("disabled")){return false;}
this.className="item selected";return false;});jQuery(container).find("ul li.item").mouseout(function(){if(jQuery(this).hasClass("disabled")){return false;}
this.className="item";return false;});jQuery(container).find("ul li.item").click(function(){if(jQuery(this).hasClass("disabled")){return false;}
self.close();self.execute(this.getAttribute("command"));return false;});};ContextMenu.prototype.setActiveStyle=function(active){var c=this.getContainer();if(c!=null){if(active==true){c.style.zIndex=DialogManager.getZIndex();}
else{c.style.display="none";DialogManager.remove(this);}}};ContextMenu.prototype.setEnabled=function(command,enabled){var container=this.getContainer();if(enabled!=false){jQuery(container).find("ul li.item[command="+command+"]").attr("class","item");}
else{jQuery(container).find("ul li.item[command="+command+"]").attr("class","item disabled");}};ContextMenu.prototype.getItems=function(enabled){var items=[];var container=this.getContainer();if(container!=null){jQuery(container).find("ul li.item").each(function(e){if(enabled==true){if(jQuery(this).hasClass("disabled")==false){items[items.length]=this;}}
else{items[items.length]=this;}});}
return items;};ContextMenu.prototype.getSelected=function(){var list=this.getItems(true);for(var i=0;i<list.length;i++){var item=list[i];if(jQuery(item).hasClass("selected")){return item;}}
if(list.length>0){return list[list.length-1];}
else{return null;}};ContextMenu.prototype.scroll=function(forward){var container=this.getContainer();var parent=jQuery(container);var selected=this.getSelected();if(selected==null){return;}
selected.className="item";var list=this.getItems();for(var i=0;i<list.length;i++){var item=list[i];if(item!=selected){continue;}
if(forward){if(i+1<list.length){list[i+1].className="item selected";}
else{list[0].className="item selected";}
break;}
else{if(i>0){list[i-1].className="item selected";}
else{list[list.length-1].className="item selected";}
break;}}};ContextMenu.prototype.open=function(event){return this.show(event);};ContextMenu.prototype.show=function(event){var container=this.getContainer();if(container!=null){var scrollTop=StyleUtil.getDocumentScrollTop();var scrollLeft=StyleUtil.getDocumentScrollLeft();var top=scrollTop+event.clientY;var left=scrollLeft+event.clientX;container.style.display="block";if(top+container.offsetHeight>(scrollTop+document.documentElement.clientHeight)){top=(top-container.offsetHeight);}
if(top<scrollTop){top=scrollTop;}
if(left+container.offsetWidth>(scrollLeft+document.documentElement.clientWidth)){left=(left-container.offsetWidth);}
if(left<scrollLeft){left=scrollLeft;}
container.style.top=top+"px";container.style.left=left+"px";this.setVisiable(true,true);}
return this;};ContextMenu.prototype.execute=function(command,event){var handler=this.context[command];if(handler!=null){handler.apply(this.context,[event]);}};ContextMenu.prototype.setup=function(items){var self=this;var context={};var html=this.build(items);var container=this.getContainer();for(var i=0;i<items.length;i++){var item=items[i];if(item!="|"){var name=item.name;context[name]=item.action;}}
this.setContent(html);this.context=context;for(var i=0;i<items.length;i++){var item=items[i];if(item!="|"&&item.key!=null){this.addShortcut(item.key,item.name);}}
jQuery(container).find("ul li.item").mouseover(function(){if(jQuery(this).hasClass("disabled")){return false;}
this.className="item selected";return false;});jQuery(container).find("ul li.item").mouseout(function(){if(jQuery(this).hasClass("disabled")){return false;}
this.className="item";return false;});jQuery(container).find("ul li.item").click(function(){if(jQuery(this).hasClass("disabled")){return false;}
self.close();self.execute(this.getAttribute("command"));return false;});};ContextMenu.prototype.build=function(items){var buffer=[];buffer[buffer.length]="<ul class=\"menu\">";for(var i=0;i<items.length;i++){var item=items[i];if(item!="|"){buffer[buffer.length]="<li class=\"item\" command=\""+item.name+"\">";if(item.icon!=null){buffer[buffer.length]="    <span class=\"icon\"><img src=\""+item.icon+"\"/></span>";}
else{buffer[buffer.length]="    <span class=\"icon\"></span>";}
buffer[buffer.length]="    <a class=\"command\" href=\"javascript:void(0)\">"+item.text+"</a>";buffer[buffer.length]="</li>";}
else{buffer[buffer.length]="<li class=\"separator\"></li>";}}
buffer[buffer.length]="</ul>";return buffer.join("");};var DialogUtil={};DialogUtil.messageTitle="Alter";DialogUtil.confirmTitle="Ensure";DialogUtil.ensureButtonText="Ok";DialogUtil.cancelButtonText="Ensure";DialogUtil.open=function(id,title,url,width,height){if(this.context==null){this.context={};}
var frameDialog=this.context[id];if(frameDialog==null){frameDialog=new FrameDialog({"container":id});}
frameDialog.open(title,url,width,height);};DialogUtil.alert=function(message,callback){var messageDialog=new MessageDialog();messageDialog.setTitle(this.messageTitle);messageDialog.setEnsureText(this.ensureButtonText);messageDialog.ensure=callback;messageDialog.open(message);};DialogUtil.confirm=function(message,callback){var confirmDialog=new ConfirmDialog();confirmDialog.setTitle(this.messageTitle);confirmDialog.setEnsureText(this.ensureButtonText);confirmDialog.setCancelText(this.cancelButtonText);confirmDialog.ensure=callback;confirmDialog.cancel=callback;confirmDialog.open(message);};window.com=com;window.KeyCode=KeyCode;window.BindUtil=BindUtil;window.EventUtil=EventUtil;window.DateUtil=DateUtil;window.StyleUtil=StyleUtil;window.StringUtil=StringUtil;window.HtmlUtil=HtmlUtil;window.ResourceLoader=ResourceLoader;window.Dialog=Dialog;window.DialogManager=DialogManager;window.TaskManager=TaskManager;window.FrameDialog=FrameDialog;window.MessageDialog=MessageDialog;window.ConfirmDialog=ConfirmDialog;window.ContextMenu=ContextMenu;window.Draggable=Draggable;window.Draggable2=Draggable2;window.Selectable=Selectable;window.DialogUtil=DialogUtil;window.I18N=I18N;})();(function(){var TabPanel=function(args){var options=(args||{});if(typeof(options.container)=="string"){this.container=document.getElementById(options.container);}
else{this.container=options.container;}
this.listeners=[];this.create();};TabPanel.prototype.getContainer=function(){return this.container;};TabPanel.prototype.create=function(){var self=this;var container=this.getContainer();var parent=jQuery(container);var labelWrap=parent.children("div.tab-label-wrap");labelWrap.find("ul").unbind();labelWrap.find("ul").click(function(event){var target=(event.target||event.srcElement);var nodeName=target.nodeName.toUpperCase();var label=jQuery(target).closest("li.tab-label");if(nodeName=="SPAN"&&target.className=="close"){self.close(label.get(0));return;}
if(label.size()>0){self.active(label.get(0));}});labelWrap.find("span.add").click(function(){self.add();});var first=labelWrap.find("ul li.tab-label:eq(0)");if(first.size()>0){this.active(first);}};TabPanel.prototype.on=function(name,listener){this.listeners.push({"name":name,"listener":listener});};TabPanel.prototype.removeListener=function(listener){for(var i=0;i<this.listeners.length;i++){var item=this.listeners[i].listener;if(item==listener){this.listeners.splice(i,1);return item;}}
return null;};TabPanel.prototype.trigger=function(name){for(var i=0;i<this.listeners.length;i++){var item=this.listeners[i];if(item.name==name){var handler=this.listeners[i].listener;handler.apply(this);}}};TabPanel.prototype.setUserObject=function(src,object){var label=null;if(typeof(src)=="string"){label=this.getLabel(tabId);}
else{label=src;}
if(label==null){return null;}
if(object==null){label.removeAttribute("data-object");}
else{label.setAttribute("data-object",JSON.stringify(object));}
return null;};TabPanel.prototype.getUserObject=function(src){var label=null;if(typeof(src)=="string"){label=this.getLabel(tabId);}
else{label=src;}
if(label==null){return null;}
var json=label.getAttribute("data-object");if(json!=null){try{return JSON.parse(json);}
catch(e){}}
return null;};TabPanel.prototype.append=function(opts){if(opts.id==null){return;}
var self=this;var container=this.getContainer();var label=document.createElement("li");var panel=document.createElement("div");var span=document.createElement("span");label.className="tab-label";panel.className="tab-panel";span.className="label";span.appendChild(document.createTextNode(opts.title));if(opts.tooltips!=null){span.setAttribute("title",opts.tooltips);}
label.appendChild(span);if(opts.closeable==true){var btn=document.createElement("span");btn.className="close";label.appendChild(btn);}
if(typeof(opts.content)=="string"){panel.innerHTML=opts.content;}
else{panel.appendChild(opts.content);}
if(opts.userObject!=null){this.setUserObject(label,opts.userObject);}
label.setAttribute("tabId",opts.id);panel.setAttribute("tabId",opts.id);jQuery(container).children("div.tab-label-wrap").children("ul").append(label);jQuery(container).append(panel);var current=this.getActiveLabel();if(current==null||opts.active!=false){this.active(label);}
return panel;};TabPanel.prototype.close=function(ele){this.remove(ele);};TabPanel.prototype.remove=function(ele){var src=jQuery(ele);var index=src.index();var tabId=src.attr("tabId");var container=src.closest("div.tab-label-wrap").parent();var active=src.hasClass("tab-active");var other=null;if(active==true){var size=src.parent().children("li").size();if((index+1)<size){other=src.parent().children("li:eq("+(index+1)+")");}
else if(index>0){other=src.parent().children("li:eq("+(index-1)+")");}}
src.remove();container.children("div.tab-panel[tabId="+tabId+"]").remove();if(other!=null&&other.size()>0){this.active(other.get(0));}
else{this.trigger("change");}};TabPanel.prototype.active=function(ele){if(typeof(ele)=="string"){var label=this.getLabel(ele);return this.active(label);}
var src=jQuery(ele);var tabId=src.attr("tabId");var container=src.closest("div.tab-label-wrap").parent();if(tabId==null||tabId==undefined){return;}
src.closest("ul").find("li.tab-label").removeClass("tab-active");src.addClass("tab-active");container.children("div.tab-panel").hide();container.children("div.tab-panel[tabId="+tabId+"]").each(function(){this.style.display="block";});if(tabId!=this.currentId){this.trigger("change");}
this.currentId=tabId;};TabPanel.prototype.size=function(){var container=this.getContainer();return jQuery(container).children("div.tab-label-wrap").find("ul li.tab-label").size();};TabPanel.prototype.getLabel=function(tabId){var container=this.getContainer();var eles=jQuery(container).children("div.tab-label-wrap").find("ul li.tab-label[tabId="+tabId+"]");if(eles.size()>0){return eles.get(0);}
return null;};TabPanel.prototype.getPanel=function(tabId){var container=this.getContainer();var eles=jQuery(container).children("div.tab-panel[tabId="+tabId+"]");if(eles.size()>0){return eles.get(0);}
return null;};TabPanel.prototype.getLabelList=function(){var container=this.getContainer();var eles=jQuery(container).children("div.tab-label-wrap").find("ul li.tab-label");var list=[];eles.each(function(){list.push(this);});return list;};TabPanel.prototype.getPanelList=function(){var container=this.getContainer();var eles=jQuery(container).children("div.tab-panel");var list=[];eles.each(function(){list.push(this);});return list;};TabPanel.prototype.getActiveLabel=function(){var container=this.getContainer();var eles=jQuery(container).children("div.tab-label-wrap").find("ul li.tab-active");if(eles.size()>0){return eles.get(0);}
return null;};TabPanel.prototype.getActivePanel=function(){var e=this.getActiveLabel();if(e!=null){return this.getPanel(e.getAttribute("tabId"));}
return null;};window.TabPanel=TabPanel;})();(function(){var DomUtil={};DomUtil.select=function(e){if(document.all){var range=document.body.createTextRange();range.moveToElementText(e);range.select();}
else if(window.getSelection){var selection=window.getSelection();if(document.createRange){var range=document.createRange();range.selectNodeContents(e);selection.removeAllRanges();selection.addRange(range);window.focus();}
else if(selection.setBaseAndExtent){selection.setBaseAndExtent(e,0,e,1);}
else{}}};DomUtil.getHandler=function(object,handler){return function(event){return handler.call(object,(event||window.event));}};DomUtil.addListener=function(target,type,handler){if(target.addEventListener){target.addEventListener(type,handler,false);}
else if(target.attachEvent){target.attachEvent("on"+type,handler);}
else{target["on"+type]=handler;}};DomUtil.removeListener=function(target,type,handler){if(target==null||type==null||handler==null){return;}
if(target.detachEvent){target.detachEvent("on"+type,handler);}
else if(target.removeEventListener){target.removeEventListener(type,handler,false);}
else{target["on"+type]=null;}};DomUtil.stop=function(event,returnValue){if(event!=null){if(event.stopPropagation){event.stopPropagation();}
else{event.cancelBubble=true;}
if(event.preventDefault){event.preventDefault();}
if(returnValue==true){event.cancel=false;event.returnValue=true;}
else{event.cancel=true;event.returnValue=false;}}
return false;};DomUtil.getStyle=function(e,name){if(e.style[name]){return e.style[name];}
else if(document.defaultView!=null&&document.defaultView.getComputedStyle!=null){var computedStyle=document.defaultView.getComputedStyle(e,null);if(computedStyle!=null){var property=name.replace(/([A-Z])/g,"-$1").toLowerCase();return computedStyle.getPropertyValue(property);}}
else if(e.currentStyle!=null){return e.currentStyle[name];}
return null;};DomUtil.getWidth=function(e){if(e.nodeName=="BODY"){return document.documentElement.clientWidth;}
var result=this.getStyle(e,"width");if(result!=null){result=parseInt(result.replace("px",""));}
return isNaN(result)?0:result;};DomUtil.getHeight=function(e){if(e.nodeName=="BODY"){return document.documentElement.clientWidth;}
var result=this.getStyle(e,"height");if(result!=null){result=parseInt(result.replace("px",""));}
return isNaN(result)?0:result;};DomUtil.show=function(id){try{var e=document.getElementById(id);if(e!=null){e.style.display="block";}}
catch(e){}};DomUtil.hide=function(id){try{var e=document.getElementById(id);if(e!=null){e.style.display="none";}}
catch(e){}};DomUtil.check=function(name,checked){var list=document.getElementsByName(name);if(list==null||list==undefined){return false;}
if(list.length==null||list.length==undefined){list.checked=checked;return true;}
for(var i=0;i<list.length;i++){list[i].checked=checked;}};var SplitPanel=function(args){var options=(args||{});if(typeof(options.container)=="string"){this.container=document.getElementById(options.container);}
else{this.container=options.container;}
this.init();};SplitPanel.prototype.init=function(){var orientation=this.container.getAttribute("split");this.left=this.getLeft();this.right=this.getRight();if(orientation=="x"){this.splitXPanel();}
else{this.splitYPanel();}};SplitPanel.prototype.splitXPanel=function(){var position=jQuery(this.container).css("position");if(position!="absolute"&&position!="relative"){this.container.style.position="relative";this.container.style.top="0px";this.container.style.left="0px";this.container.style.width="100%";this.container.style.overflow="hidden";}
var width=this.getWidth(this.container);var height=this.getHeight(this.container);var leftWidth=this.getWidth(this.left);var leftHeight=this.getHeight(this.left);this.frame=document.createElement("div");this.frame.className="split-frame";this.frame.style.position="absolute";this.frame.style.top="0px";this.frame.style.left="0px";this.frame.style.width="100%";this.frame.style.height="100%";this.frame.style.backgroundColor="transparent";this.frame.style.zIndex=190;this.frame.style.cursor="col-resize";this.frame.style.display="none";this.container.appendChild(this.frame);this.split=document.createElement("div");this.split.className="split-bar";this.split.style.position="absolute";this.split.style.top="0px";this.split.style.left=(leftWidth-4)+"px";this.split.style.width="9px";this.split.style.height="100%";this.split.style.backgroundColor="transparent";this.split.style.cursor="col-resize";this.split.style.zIndex=199;this.container.appendChild(this.split);this.left.style.zIndex=98;this.right.style.zIndex=98;this.start=function(event){var src=(event.srcElement||event.target);var keyCode=(event.keyCode||event.which);if(keyCode!=1){return true;}
DomUtil.addListener(document,"mouseup",this.stopHandler);DomUtil.addListener(document,"mousemove",this.moveHandler);this.frame.style.display="block";this.split.style.backgroundColor="rgba(0, 0, 0, 0.2)";return DomUtil.stop(event);};this.move=function(event){var offset=jQuery(this.container).offset();var x=event.clientX-offset.left-4;if(x<=0){return;}
this.split.style.left=x+"px";this.left.style.width=(x+4)+"px";this.right.style.marginLeft=(x+4)+"px";return DomUtil.stop(event);};this.stop=function(event){var width=jQuery(this.container).width();var leftWidth=this.split.offsetLeft+4;this.left.style.width=leftWidth+"px";this.right.style.marginLeft=leftWidth+"px";this.split.style.backgroundColor="transparent";this.frame.style.display="none";DomUtil.removeListener(document,"mouseup",this.stopHandler);DomUtil.removeListener(document,"mousemove",this.moveHandler);var flag=DomUtil.stop(event);if(this.callback!=null){this.callback();}
return flag;};DomUtil.removeListener(this.split,"mousedown",this.mousedownHandler);this.moveHandler=DomUtil.getHandler(this,this.move);this.stopHandler=DomUtil.getHandler(this,this.stop);this.mousedownHandler=DomUtil.getHandler(this,this.start);DomUtil.addListener(this.split,"mousedown",this.mousedownHandler);};SplitPanel.prototype.getLeft=function(){if(this.left==null){var list=this.getChildNodes(this.container);this.left=list[0];}
return this.left;};SplitPanel.prototype.getRight=function(){if(this.right==null){var list=this.getChildNodes(this.container);this.right=list[1];}
return this.right;};SplitPanel.prototype.getWidth=function(e){if(e.nodeName=="BODY"){return document.documentElement.clientWidth;}
else{return jQuery(e).width();}};SplitPanel.prototype.getHeight=function(e){if(e.nodeName=="BODY"){return document.documentElement.clientHeight;}
else{return jQuery(e).height();}};SplitPanel.prototype.getChildNodes=function(c){var list=[];var childNodes=c.childNodes;for(var i=0;i<childNodes.length;i++){var node=childNodes[i];if(node.nodeType==1){list[list.length]=node;}}
return list;};SplitPanel.prototype.getContainer=function(){return this.container;};window.SplitPanel=SplitPanel;})();