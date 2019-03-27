// (c)2018 www.finderweb.net

(function(){var treeId="htree";HTree.click=function(src){var node=HTree.getNode(src);var url=src.getAttribute("data");HTree.setActive(node);if(url==null||url.length<1){return;}
if(url=="javascript:void(0);"){return;}
try{HTree.open(url);}
catch(e){if(typeof(window.console)!="undefined"){window.console.error(e.name+": "+e.message);}
alert("系统错误，请稍后再试！");}};HTree.open=function(url){var params=ParameterParser.parse(url);var host=params.getParameter("host");var workspace=params.getParameter("workspace");var path=(params.getParameter("path")||"");var title=FileType.getName(path);var tooltips=host+"@"+workspace;if(title==""||title=="/"){title=workspace;}
else{tooltips=tooltips+"/"+path}
jQuery("#welcome-panel").remove();jQuery("#tab-panel-container").show();var label=window.tabPanel.getLabel("finder-panel");var span=jQuery(label).find("span.label");span.html(title);span.attr("title",tooltips);window.tabPanel.active(label);Finder.load(host,workspace,path);};function reload(path){var root=HTree.Util.getRootNode(document.getElementById(treeId));if(root==null){return;}
var n=root;var a=getNodePaths(path);for(var i=0;i<a.length;i++){n=getTreeNodeByValue(n,a[i]);if(n==null){break;}}
if(n!=null){HTree.reload(n);}};function expand(path){var root=HTree.Util.getRootNode(document.getElementById(treeId));if(root==null){return;}
var a=getNodePaths(path);var handler=function(node,i){if(i>=a.length){HTree.setActive(node);return;}
var e=getTreeNodeByValue(node,a[i]);if(e!=null){var height=document.documentElement.clientHeight;var scrollTop=document.body.scrollTop;var offsetTop=e.offsetTop;if(scrollTop>offsetTop){document.body.scrollTop=offsetTop-Math.floor(height/2);document.documentElement.scrollTop=offsetTop-Math.floor(height/2);}
if(offsetTop>(height+scrollTop)){document.body.scrollTop=offsetTop-Math.floor(height/2);document.documentElement.scrollTop=offsetTop-Math.floor(height/2);}
HTree.expand(e,{"expand":true,"callback":function(e){handler(e,i+1);}});}
else{console.log("node ["+a[i]+"] not found, reload...");HTree.reload(node);}};handler(root,0);};function getNodePaths(path){var a=[];var s=path.split("/");for(var i=0;i<s.length;i++){s[i]=HTree.trim(s[i]);if(s[i].length>0){a[a.length]=s[i];}}
return a;};function getTreeNodeByValue(node,value){if(node==null){return null;}
var list=getChildTreeNodes(node);var length=list.length;if(value.charAt(0)=="["&&value.charAt(value.length-1)=="]"){var index=parseInt(value.substring(1,value.length-1));if(isNaN(index)){return null;}
for(var i=0;i<length;i++){var a=HTree.Util.getChildNode(list[i],"//a");if(i==index){return list[i];}}
return null;}
for(var i=0;i<length;i++){var a=HTree.Util.getChildNode(list[i],"//a");if(a!=null&&a.getAttribute("value")==value){return list[i];}}
return null;};function getChildTreeNodes(node){var c=null;var n=node.nextSibling;while(n!=null){if(n.nodeType==1){c=n;break;}
else{n=n.nextSibling;}}
var temp=[];if(c!=null){var list=c.childNodes;var length=list.length;for(var i=0;i<length;i++){n=list[i];if(n.nodeType==1&&n.className!=null&&n.className.indexOf("node")>-1){temp[temp.length]=n;}}}
return temp;};function buildTree(id,xmlUrl,rootUrl){var e=document.getElementById(id);if(e==null){return;}
var tree=new HTree.TreeNode({text:"Finder",href:rootUrl,xmlSrc:xmlUrl});tree.load(function(){HTree.setActive(null);this.render(document.getElementById(id));expand("/[0]");});};var FileTree={};FileTree.expand=function(){var win=Finder.getWindow();var leftFrame=win.leftFrame;if(leftFrame!=null&&leftFrame.expand!=null){leftFrame.expand("/"+Finder.getHost()+"/"+Finder.getWorkspace()+Finder.getPath());}};FileTree.reload=function(){var win=Finder.getWindow();var leftFrame=win.leftFrame;if(leftFrame!=null&&leftFrame.reload!=null){leftFrame.reload("/"+Finder.getHost()+"/"+Finder.getWorkspace()+Finder.getPath());}};FileTree.expand=function(path){expand(path);};FileTree.reload=function(path){reload(path);};jQuery(function(){var requestURI=window.location.pathname;HTree.config.stylePath=requestURI+"?action=res&path=/htree/middle/";buildTree(treeId,requestURI+"?action=finder.getHostXml","");});window.FileTree=FileTree;})();