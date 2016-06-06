<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html" indent="yes" version="4.01" encoding="ISO-8859-1" doctype-public="-//W3C//DTD HTML 4.01//EN"/>
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Feb 02, 2012</xd:p>
            <xd:p><xd:b>Author:</xd:b> Abderrazek Boufahja, IHE Development, Kereval</xd:p>
        </xd:desc>
    </xd:doc>
    <xsl:param name="viewdown">false</xsl:param>
    <xsl:param name="constraintPath">#</xsl:param>
    
    <xsl:template match="/">
        <html  xmlns="http://www.w3.org/1999/xhtml">
            <head>
                <title>External Validation Report</title>
            </head>
            <body>
                <script type="text/javascript">
                    function hideOrViewValidationDetailsMB() {
                       var detaileddiv = document.getElementById('resultdetailedann');
                       if (detaileddiv != null){
                           var onn = document.getElementById('resultdetailedann_switch_on');
                           if (onn != null){
                               if (onn.style.display == 'block') onn.style.display = 'none';
                               else if (onn.style.display == 'none') onn.style.display = 'block';
                           }
                           var off = document.getElementById('resultdetailedann_switch_off');
                           if (off != null){
                               if (off.style.display == 'block') off.style.display = 'none';
                               else if (off.style.display == 'none') off.style.display = 'block';
                           }
                           var body = document.getElementById('resultdetailedann_body');
                           if (body != null){
                               if (body.style.display == 'block') body.style.display = 'none';
                               else if (body.style.display == 'none') body.style.display = 'block';
                           }
                       }
                    }
                    
                    function hideOrUnhideMB(elem){
                        var elemToHide = document.getElementById(elem.name + '_p');
                        if (elemToHide != null){
                                if (elem.checked){
                                    elemToHide.style.display = 'none';
                                }
                                else{
                                    elemToHide.style.display = 'block';                                
                                }
                           }
                    }
                    
                    function detectDownloadResultButton(){
                        var elemDown = document.getElementById('resultForm:downloadMBResultB');
                        if (elemDown != null) return true;
                        return false;
                    }
                    
                    function extractDownloadResultButton(parentNoeud){
                        var elemDown = document.getElementById('resultForm:downloadMBResultB').cloneNode(true);
                        if (elemDown != null) {
                            var downloadspan = document.getElementById('downloadspan');
                            downloadspan.appendChild(elemDown);
                        }
                    }
                    
                </script>
                <style type="text/css">
                    .moore {
                        color: #005500;
                        background-color: #99bbee; 
                   }
                   .rich-panel{background-color:#F3F2F2;border-color:#C0C0C0;}
                   .rich-panel-header{background-color:#4A75B5;border-color:#4A75B5;font-size:11px;color:#FFF;font-weight:bold;font-family:Arial,Verdana,sans-serif;background-image:url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAyCAYAAABLXmvvAAAAqElEQVR42u3NsQpBAQAF0Pt9ymBQBmUwGJRBvcHwiqJeURSDwWAwGAwGQr1EKClJSUlKyndcn3ENdzjzQXf/pQLamw8V0IzfVEC0fFEB9dmTCqhOH1RAOLlTAZXxjQoIRlcqoDy8UAGlwZkKKPZPVEChd6QC8p0DFZBr7aiAbLSlAjKNNRWQrsVUQCpcUQHJyoIKSARzKjh27NixY8eOHTt27Nix4z+Of0GzTliPaNOMAAAAAElFTkSuQmCC);}
                   .rich-panel-body{font-size:11px;color:#000;font-family:Arial,Verdana,sans-serif;}
                   .rich-panel{border-width:1px;border-style:solid;padding:1px;}
                   .rich-stglpanel-header{border-width:1px;border-style:solid;cursor:pointer;padding:2px;background-position:top left;background-repeat:repeat-x;}
                   .rich-stglpanel-body{padding:10px;text-align:left;vertical-align:top;overflow:auto;}
                   .rich-stglpanel-header{background-color:#4A75B5;border-color:#4A75B5;font-size:11px;color:#FFF;font-weight:bold;font-family:Arial,Verdana,sans-serif;}
                   .rich-stglpanel-body{background-color:#F3F2F2;font-size:11px;font-family:Arial,Verdana,sans-serif;}
                   .rich-stglpanel-header{background-image:url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAB4AAAAyCAYAAABLXmvvAAAAqElEQVR42u3NsQpBAQAF0Pt9ymBQBmUwGJRBvcHwiqJeURSDwWAwGAwGQr1EKClJSUlKyndcn3ENdzjzQXf/pQLamw8V0IzfVEC0fFEB9dmTCqhOH1RAOLlTAZXxjQoIRlcqoDy8UAGlwZkKKPZPVEChd6QC8p0DFZBr7aiAbLSlAjKNNRWQrsVUQCpcUQHJyoIKSARzKjh27NixY8eOHTt27Nix4z+Of0GzTliPaNOMAAAAAElFTkSuQmCC);}
                   .rich-stglpanel{border-width:1px;border-style:solid;padding:1px;background-position:top right;background-repeat:no-repeat;}
                   .rich-stglpanel{background-color:#F3F2F2;border-color:#C0C0C0;}
                </style>
                <style type="text/css">
                    /*********** resultStyle.css *************/
                    .PASSED{
                    color:green;
                    font-weight: bold;
                    }
                    
                    .FAILED{
                    color:red;
                    font-weight: bold;
                    }
                    
                    .ABORTED{
                    color:orange;
                    font-weight: bold;
                    }
                    
                    .Error {
                    background-color: #FFCCCC;
                    }
                    
                    .Warning {
                    background-color: #FFCC33;
                    }
                    
                    .Note {
                    background-color: #F6EF8D;
                    }
                    
                    .Condition {
                    background-color: #F6EF8D;
                    }
                    
                    .Unknown {
                    background-color: white;
                    }
                    
                    .Report {
                    background-color: #CCFFCC;
                    }
                    
                    .styleResultBackground
                    {
                    background-color : #C3E1F2;
                    }
                    
                    .errortwo
                    {
                    background-color : #FFCCCC;	
                    width:100%;
                    }
                    .warning
                    {
                    background-color : #FFCC33;
                    }
                    .note
                    {
                    background-color : #F6EF8D;
                    }
                    .condition
                    {
                    background-color : #F6EF8D;
                    }
                    .unknown
                    {
                    background-color : white;
                    }
                    .report
                    {
                    background-color : #CCFFCC;
                    width:100%;
                    }
                    .dvtWarning
                    {
                    color:orange; 
                    font-weight:bold;
                    }
                    .dvtError
                    {
                    color:red;
                    font-weight:bold;
                    }
                    .dvtConditionText
                    {
                    font-style:italic;
                    }
                </style>
                <style type="text/css">
                 /******* theme.css *****************************/
                 /******* hyperlink and anchor tag styles *******/
                   a:link,a:visited {
                   color:#005FA9;
                   text-decoration:none;
                   }
                   
                   /************** header tag styles **************/
                   h1 {
                   font:bold 120% Arial,sans-serif;
                   margin:0;
                   padding:0;
                   }
                   
                   h2 {
                   font:bold 114% Arial,sans-serif;
                   color:#069;
                   margin:0;
                   padding:0;
                   }
                   
                   h3 {
                   font:bold 100% Arial,sans-serif;
                   color:#334d55;
                   margin:0;
                   padding:0;
                   }
                   
                   h4 {
                   font:100% Arial,sans-serif;
                   color:#333;
                   margin:0;
                   padding:0;
                   }
                   
                   h5 {
                   font:100% Arial,sans-serif;
                   color:#334d55;
                   margin:0;
                   padding:0;
                   }
                   
                   /*************** list tag styles ***************/
                   ul {
                   list-style-type:square;
                   }
                   
                   ul ul {
                   list-style-type:disc;
                   }
                   
                   ul ul ul {
                   list-style-type:none;
                   }
                   
                   label {
                   font:bold 1em Arial,sans-serif;
                   color:#334d55;
                   }
                   
                   input {
                   font-family:Arial,sans-serif;
                   }
                   
                   /**********************************************
                    Layout Divs                                 
                   **********************************************/
                   #pagecell1 {
                   position:absolute;
                   top:95px;
                   left:10px;
                   right:10px;
                   background :#fff;
                   }
                   
                   #tl {
                   position:absolute;
                   top:-1px;
                   left:-1px;
                   z-index:100;
                   margin:0;
                   padding:0;
                   }
                   
                   #tr {
                   position:absolute;
                   top:-1px;
                   right:-1px;
                   z-index:100;
                   margin:0;
                   padding:0;
                   }
                   
                   #masthead {
                   position: absolute;
                   z-index:50; 
                   top:5px;
                   left:10px;
                   right:10px;
                   max-height: 80px;
                   }
                   
                   #pageNav {
                   float:left;
                   width:178px;
                   background-color:#F5f7f7;
                   border-right:1px solid #ccc;
                   border-bottom:1px solid #ccc;
                   font:small Verdana,sans-serif;
                   padding:2px;
                   }
                   
                   #content {
                   margin:0;
                   padding:5px 5px;
                   background: #fff;
                   /*position:relative;*/
                   }
                   
                   /**********************************************
                    Component Divs                              
                   **********************************************/
                   #siteName {
                       color:#fff;
                   
                   }
                   
                   #panelStyle {
                   position:absolute;
                   left:2%;
                   right:2%;
                   width:95.6%;
                   }
                   
                   /************** utility styles *****************/
                   #utility {
                       z-index:90; 
                   }
                   
                   a.topbarLink:link, a.topbarLink:visited {
                       color:#FFFFFF;
                   }
                   
                   .menuLink {
                       color:#000000;
                       text-decoration:none;
                   }
                   
                   a.menuLink:link, a.menuLink:visited {
                       color:#000000;
                       text-decoration:none;
                   }
                   
                   /************** pageName styles ****************/
                   #pageName {
                   border-bottom:1px solid #ccd2d2;
                   margin:0;
                   padding:0 0 14px 10px;
                   }
                   
                   #pageName h2 {
                   font:bold 175% Arial,sans-serif;
                   color:#000;
                   margin:0;
                   padding:0;
                   }
                   
                   #pageName img {
                   position:absolute;
                   top:8px;
                   right:6px;
                   margin:0;
                   padding:0;
                   }
                   
                   /************* globalNav styles ****************/
                   #globalNav {
                   /*
                   position:absolute;
                   top:34px;
                   width:100%;
                   min-width:640px;
                   height:32px;
                   background-image:url(/ProductRegistry/img/glbnav_background.gif);
                   color:#ccc;
                   margin:0;
                   padding:0;
                   */
                       }
                   
                   #gnl {
                   position:absolute;
                   top:0; 
                   left:0;
                   }
                   
                   #gnr {
                   position:absolute;
                   top:0;
                   right:0;
                   }
                   
                   #globalLink {
                   }
                   
                   a.glink,a.glink:visited {
                   font-size:small;
                   color:#000;
                   font-weight:700;
                   border-right:1px solid #8FB8BC;
                   margin:0;
                   padding:2px 5px 4px;
                   }
                   
                   a.glink:hover {
                   background-image:url(../img/glblnav_selected.gif);
                   text-decoration:none;
                   }
                   
                   .skipLinks {
                   display:none;
                   }
                   
                   /************ subglobalNav styles **************/
                   .subglobalNav {
                   position:absolute;
                   top:84px;
                   left:0;
                   min-width:640px;
                   height:20px;
                   visibility:hidden;
                   color:#fff;
                   padding:0 0 0 10px;
                   }
                   
                   .subglobalNav a:link,.subglobalNav a:visited {
                   font-size:80%;
                   color:#fff;
                   }
                   
                   .subglobalNav a:hover {
                   color:#ccc;
                   }
                   
                   /*************** search styles *****************/
                   #search {
                   position:absolute;
                   top:0px;
                   right:10px;
                   z-index:101;
                   }
                   
                   #search input {
                   font-size:70%;
                   margin:0 0 0 10px;
                   }
                   
                   #search a:hover {
                   margin:0;
                   }
                   
                   /************* breadCrumb styles ***************/
                   #breadCrumb {
                   font:small Verdana,sans-serif;
                   color:#005FA9;
                   padding:5px 0 5px 10px;
                   }
                   
                   #breadCrumb a {
                   color:#005FA9;
                   }
                   
                   #breadCrumb a:hover {
                   color:#005FA9;
                   text-decoration:underline;
                   }
                   
                   /************** feature styles *****************/
                   .feature {
                   font-size:80%;
                   min-height:200px;
                   height:200px;
                   padding:0 0 10px 10px;
                   }
                   
                   /*** html>body .feature {height: auto;} ***/
                   .feature h3 {
                   font:bold 175% Arial,sans-serif;
                   color:#000;
                   padding:30px 0 5px;
                   }
                   
                   .feature img {
                   float:left;
                   padding:0 10px 0 0;
                   }
                   
                   /*************** story styles ******************/
                   .story {
                   font-size:80%;
                   padding:10px 0 0 10px;
                   }
                   
                   .story h3 {
                   font:bold 125% Arial,sans-serif;
                   color:#000;
                   }
                   
                   .story p {
                   padding:0 0 10px;
                   }
                   
                   .story a.capsule {
                   font:bold 1em Arial,sans-serif;
                   color:#005FA9;
                   display:block;
                   padding-bottom:5px;
                   }
                   
                   td.storyLeft {
                   padding-right:12px;
                   }
                   
                   /************** siteInfo styles ****************/
                   #siteInfo {
                   clear:both;
                   font-size:small;
                   color:#005fa9;
                   margin-top:0;
                   padding:0 0;
                   /*top:20px;*/
                   /*left:2%;
                   right:2%;
                   width:95.6%;*/
                   width:100%;
                   position:relative;
                   -position: fixed;
                   -z-index:-1000 ; /* for IE6 */
                   /z-index:-1000 ; /* for IE7 */
                   }
                   
                   #siteInfo a {
                   color:#FFF;
                   }
                   
                   #siteInfo img {
                   vertical-align:middle;
                   padding:4px 4px 4px 0;
                   }
                   
                   #sectionLinks h3 {
                   border-bottom:1px solid #ccc;
                   padding:10px 0 2px 10px;
                   }
                   
                   #sectionLinks a:link,#sectionLinks a:visited {
                   display:block;
                   border-top:1px solid #fff;
                   border-bottom:1px solid #ccc;
                   background-image:url(../img/bg_nav.jpg);
                   font-weight:700;
                   color:#21536A;
                   padding:3px 0 3px 10px;
                   }
                   
                   #sectionLinks a:hover {
                   border-top:1px solid #ccc;
                   background-color:#DEF;
                   background-image:none;
                   font-weight:700;
                   text-decoration:none;
                   }
                   
                   /************* relatedLinks styles **************/
                   .relatedLinks {
                   border-bottom:1px solid #ccc;
                   margin:0;
                   padding:0 0 10px 10px;
                   }
                   
                   .relatedLinks h3 {
                   padding:10px 0 2px;
                   }
                   
                   /**************** advert styles *****************/
                   #advert {
                   padding:10px;
                   }
                   
                   /********************* end *********************/
                   
                   
                   
                   
                   input, textarea {
                       border: 1px solid #BBBBBB;
                       font-size: 10px;
                       background: #F0F8FF;
                       color: black; 
                   }
                   
                   
                   .commandButton, input[type="submit"], input[type="file"]
                   {
                       background-color: #4477AA;
                       color: white;
                       margin: 5px;
                       
                       /margin:3px ;/* for IE 7 */
                       /padding-left : 5px ;/* for IE 7 */
                       /padding-right : 5px ;/* for IE 7 */
                       
                       -margin:3px ;/* for IE 6 */
                       -padding-left : 5px ;/* for IE 6 */
                       -padding-right : 5px ;/* for IE 6 */
                       border-color: gray;
                   }
                   
                   .commandButtonDisabled
                   {
                       background-color: #BBBBBB;
                       color: white;
                       margin: 5px;
                       
                       /margin:3px ;/* for IE 7 */
                       /padding-left : 5px ;/* for IE 7 */
                       /padding-right : 5px ;/* for IE 7 */
                       
                       -margin:3px ;/* for IE 6 */
                       -padding-left : 5px ;/* for IE 6 */
                       -padding-right : 5px ;/* for IE 6 */
                       border-color: gray;
                   }
                   
                   .tableControl, .actionButtons {
                       width: 100%;
                   }
                   
                   .tableControl {
                       text-align: right;
                   }
                   
                   .footer {
                       text-align: center;
                       font-size: 10px;
                   }
                   
                   
                   .columnHeader:hover {
                   color:#F60;
                   }
                   
                   .message {
                   border:1px solid #FC0;
                   margin-top:5px;
                   margin-bottom:5px;
                   background-color:#F0F8FF;
                   font-size:12px;
                   padding:5px;
                    
                   }
                   .rowEditTemplate
                   {
                     height: 27px;
                   }
                   
                   .name {
                   vertical-align: middle ;
                   font-weight:700 ;
                    width:125px ;
                    
                   }
                   .nameColumn{
                       text-align: left;
                   }
                   
                   .value {	
                   vertical-align: middle ;
                   padding:5px;
                   -border-top:10px;
                    
                   }
                   
                   .error {
                   float: left ;
                   margin-left:20px;
                   padding:5px;
                   vertical-align: middle ;
                   
                   }
                   
                   .errorsNoBorder {
                   color:red;
                   vertical-align:middle;
                   vertical-align: middle ;
                   }
                   
                   .errors {
                   color:red;
                   vertical-align: middle ;
                   }
                   
                   .valid {
                   color:ForestGreen;
                   padding-left: 10px;
                   font-weight:bold;
                   vertical-align: middle;
                   }
                   
                   .required {
                   color:red;
                   padding-left:2px;
                   }
                   
                   .tableEditTemplate
                   {
                       border-width: 0px ;
                   }
                   .nameColumn
                   {
                       width:  135px;
                   }
                   
                   /*********** Add Icons  : jchatel **********/
                   .iconHome {
                   margin-top:10px;
                   height:auto;
                   }
                   
                   .iconHome img {
                   height:120px;
                   vertical-align:middle;
                   margin-left:auto;
                   margin-right:auto;
                   display:block;
                   }
                   
                   /********* richfaces*********************/
                   .rich-table {
                   width:100%;
                   }
                   
                   .rich-inplace-select-list-decoration {
                   background-color:#ecf4fe;
                   }
                   
                   /*.rich-inplace-select-shadow {
                   background-color:#ecf4fe;
                   }*/
                   
                   .rich-inplace-select-field {
                   font:1em Arial,sans-serif;
                   color:#334d55;
                   }
                   
                   .rich-inplace-select-selected-item {
                   font:1em Arial,sans-serif;
                   color:#334d55;
                   }
                   
                   .rich-calendar-tool-btn {
                   font-family:Arial, Verdana;
                   }
                   
                   a:hover,#utility a:hover,.story a.capsule:hover {
                   text-decoration:underline;
                   }
                   
                   form,#sectionLinks {
                   margin:0;
                   padding:0;
                   vertical-align : middle;
                   }
                   
                   #utility a,#search a:link,#search a:visited {
                   color:#fff;
                   }
                   
                   .relatedLinks a,#advert img {
                   display:block;
                   }
                   
                   .errors input,.errors textarea {
                   border:1px solid red;
                   }
                   
                   
                   
                   /********* Tooltip for help (from http://livedemo.exadel.com/richfaces-demo/richfaces/toolTip.jsf;jsessionid=08B5F56E1258799B9CE7C79B1300E858?c=toolTip )*********************/
                   
                   .tooltip {
                               background-color:#{richSkin.generalBackgroundColor};
                               border-width:3px;
                               padding:10px;
                               color: #000000;
                   }
                   
                   .tooltip-text {
                               width:350px;
                               height:80px;
                               cursor:arrow;
                               display: table-cell;
                               vertical-align: middle;
                               margin-left:10px;
                   }
                   
                   .tooltipData {
                               font-weight: bold;
                   }
                   
                   .panelForCriteria {
                       		font:140% white;
                               /* background-color: green;    Color used to work on layout (to determinate your css/panel sizes)*/
                       		background-color: none;
                       		background: none; 
                       		padding:-8px -8px -8px -8px;
                       		margin-left:-8px;
                       		margin-right:0;
                       		margin-top:-12px;
                       		margin-bottom:-10px;
                       		padding-left: -8px; 
                       		border: 0;
                       		/* font-size: 10px;
                       			color: black; 
                       			
                   dvdstore/view/screen.css:        
                       		background: url(img/nav.tab.left.gif) no-repeat left top;
                       		background: url(img/nav.tab.right.gif) no-repeat right top;
                       */
                   }
                   
                   .imageRemoveCriteriaForPanelInAdvancedSearch
                    {
                       		margin-left:-15px;
                       		margin-right:-15px;
                       		margin-top: 2px;
                       		cursor: pointer;
                               border: 0;
                   }
                   
                   .imageAddCriteriaForPanelInAdvancedSearch
                    {
                       		margin-left:-15px;
                       		margin-right:-15px;
                       		margin-top: 2px;
                       		cursor: pointer;
                               border: 0;
                   }
                   
                   
                   .coloredBarForCriteriaAdvancedSearch {
                       		
                       		/*background: url(../img/blueBarStripe.gif) repeat; background-size: auto ;*/
                       		/* grey - background-color : #ACA9AC ; */
                       		background-color : #4A76B5 ;
                       		padding-top : 6px;
                       		-moz-border-radius : 10px ;
                       		-webkit-border-radius :10px ;
                       		border-radius :10px ;	
                       		
                       		/*background-color: grey;*/
                       		width: 800px;
                       		height: 40px;
                       		
                   }
                   
                   
                   .coloredBarForCriteriaEasySearch {
                       		
                       		/*background: url(../img/blueBarStripe.gif) repeat; background-size: auto ;*/
                       		/* grey - background-color : #ACA9AC ; */
                       		background-color : #4A76B5 ;
                       		/** blue ciel - background-color : #88B8DD; */
                       		padding-top : 6px;
                       		-moz-border-radius : 10px  ;
                       		-webkit-border-radius :10px ;
                       		border-radius :10px ;	
                       		width: 800px;
                       		 
                       		
                       		/*background-color: grey;*/
                       		/*jrc height: 140px;*/
                       		
                   }
                   
                   
                   
                   .whiteDivForButtonsDuringSearch {
                       		
                       		/*background: url(../img/blueBarStripe.gif) repeat; background-size: auto ;*/
                       		/* grey - background-color : #ACA9AC ; */
                       		background-color : white ;
                       		/** blue ciel - background-color : #88B8DD; */
                       		padding-top : 6px;
                       		-moz-border-radius : 10px  ;
                       		-webkit-border-radius :10px ;
                       		border-radius :10px ;
                       		border:3px solid #4A76B5;
                       		
                       		/*background-color: grey;*/
                       		width: 800px;
                       		/*jrc height: 140px;*/
                       		
                   }
                   
                   .radiusStyleInCorners {
                       		
                       		-moz-border-radius : 10px  ;
                       		-webkit-border-radius :10px ;			 
                       		border-radius :10px ;	
                   }
                   
                   .radiusStyle25InCorners {
                       		
                       		-moz-border-radius : 25px  ;
                       		-webkit-border-radius :25px ;			 
                       		border-radius :25px ;	
                   }
                   
                   
                   .crawlerGreenPanel {
                       		
                       		-moz-border-radius : 30px  ;
                       		-webkit-border-radius :30px ;			 
                       		border-radius :30px ;
                       		
                       		border:5px solid #55D400;
                       			
                   }
                   
                   
                   .crawlerRedPanel {
                       		
                       		-moz-border-radius : 30px  ;
                       		-webkit-border-radius :30px ;			 
                       		border-radius :30px ;
                       		
                       		border:5px solid red;
                       			
                   }
                   
                   .crawlerSummary {
                       		
                       		-moz-border-radius : 10px  ;
                       		-webkit-border-radius :10px ;			 
                       		border-radius :10px ;
                       		
                       		width : 180px;
                       		border:2px solid grey;
                       		background-color: #FFFFFF;
                       		padding: 0px;			
                   }
                   
                   .importButton {
                       		
                       		-moz-border-radius : 8px  ;
                       		-webkit-border-radius :8px ;			 
                       		border-radius :8px ;
                       		
                       		width : 150px;
                       		border:2px solid #3265FA;
                       		background-color: #FFFFFF;
                       		padding: 0px;			
                   }
                   
                   
                   .test {
                       		font-style:italic;	
                   }
                   
                   .commandButtonWhite
                   {
                       background-color: #FFFFFF;
                       color: #4A76B5;
                       margin: 5px;
                       
                       /margin:3px ;/* for IE 7 */
                       /padding-left : 5px ;/* for IE 7 */
                       /padding-right : 5px ;/* for IE 7 */
                       
                       -margin:3px ;/* for IE 6 */
                       -padding-left : 5px ;/* for IE 6 */
                       -padding-right : 5px ;/* for IE 6 */
                       border-color: gray;
                   }
                   
                   
                   .coloredBarForCriteriaLeft
                   {
                       		padding-top:-3px;
                       		padding-bottom: 14px;
                       		margin-top:-5px;
                       		margin-bottom:-5px;
                       		margin-left: -20px;
                       		height: 100%;
                       		background-color: none;
                       		background: none; 
                       		/*background: url(../img/corners_criteria_bar_left.gif) no-repeat left top;*/
                       		
                   }
                   .coloredBarForCriteriaRight {
                       		
                       		padding-top:0px;
                       		padding-bottom: 0px;
                       		margin-top:-5px;
                       		margin-bottom:-10px;
                       		margin-right: -15px;
                       		height: 100%;
                       		background-color: none;
                       		background: none; 
                       		/*background: url(../img/corners_criteria_bar_right.gif) no-repeat right top; */
                       /*background-color: green; */
                   }
                   
                   .dataListForNewCriteriaInAdvancedSearchPR {
                       		/*margin-top:-44px; */
                       		list-style-type: none;
                   }	
                   
                   .dataListForAllSelectedCriteriasInAdvancedSearchPR {
                       		list-style-type: none;
                       	
                   }	
                   
                   
                   .panelForActionButton {
                       
                       		font:140% white;
                               background-color: green;   /* Color used to work on layout (to determinate your css/panel sizes)*/
                       		/*background-color: none;*/
                       		background: none; 
                       		padding:-8px -8px -8px -8px;
                       		margin-left:-8px;
                       		margin-right:0;
                       		margin-top:-10px;
                       		margin-bottom:-10px;
                       		padding-left: -8px; 
                       		border: 0;	
                   }
                   
                   
                   
                   .panelForPRHomePage {
                   
                       margin:-5px;
                       padding:-10px;
                       background-color: none; 
                       background: none; 
                       border: none;
                   }
                   
                   
                   
                   .panelStyleForFileUpload {
                   
                       margin:-5px;
                       padding:-10px;
                       background-color: none; 
                       background: none; 
                       border: none;
                   }
                   
                   
                   
                   /** Style for Tree */
                   .rich-tree{
                   
                       text-align: left;
                       
                   
                   }
                   
                   
                   /** Style for modal panel */
                   /* = Downloads modal dialog styles (this has replaced stylesheet: modal.css) */
                   
                   #modallayer {
                       background-color: transparent;
                       position: absolute;
                       width: 100%;
                       height: 100%;
                       top: 0px;
                       left: 0px;
                       z-index: 1000;
                      text-align: left;
                       background-image: url(/theme/images/common/background_trans.gif);
                   }
                   
                   #dialog {
                       position: relative;
                       width: 440px;
                       min-height: 100px;
                      margin-top: 100px;
                      border:1px solid #94aebd;
                       background-color: #f3f3f3;
                       background-image: url(/theme/images/common/modaldialog_infoicon.gif);
                       background-repeat: no-repeat;
                       background-position: 0px 25px;
                      text-align: left;
                   }
                   
                   #modallayer > #dialog {
                       position:fixed;
                   }
                   
                   #modallayer h4 {
                           background-image: url(/img/modal/modaldialog_hdrbkg.gif);
                           background-repeat:repeat-x;
                           background-color: #d5d5d5;
                           font-weight:bold;
                           font-size:15px;
                           color:#243446;
                           height:28px;
                           text-indent: 20px;
                           margin:0px;
                           padding-top: 15px;
                   }
                   
                   #modallayer p {
                           padding: 2px 10px 2px 110px;
                   
                   }
                   
                   #modallayer #buttonrow {
                           background-image: url(/img/modal/modaldialog_footrbkg.gif);
                           background-repeat:repeat-x;
                           background-color: #d5d5d5;
                           color:#243446;
                           height:33px;
                           padding-left: 110px;
                           padding-top: 10px;
                           margin:0px;
                           border-top: none;
                   }
                   
                   #modallayer #buttonrow ul {
                           padding-left: 0px;
                           margin-left: 0px;
                           display: inline;
                   }
                   
                   #modallayer #buttonrow ul li {
                           list-style: none;
                           display: inline;
                           padding-right: 4px;
                   }
                   
                   #modallayer #buttonrow .hrefbuttons {
                           background-color:#4A5D75;
                           border-color:#94AEBD #233345 #233345 #94AEBD;
                           border-style:solid;
                           border-width:1px;
                           color:#FFFFFF;
                           cursor:pointer;
                           font-size:10px;
                           font-weight:bold;
                           padding: 4px 10px;
                           text-decoration:none;
                   }
                   .ti-row1 {  
                       background-color: #EDEBEB;  
                   }  
                   .ti-row2 {  
                        background-color: #ffffff;  
                   } 
                   .row-red {  
                       background-color: #FFCCCC;  
                   }  
                   .row-blue {  
                       background-color: #EAF0F8;  
                   }  
                   .row-yellow {  
                       background-color: #FFFF66;  
                   }  
                </style>
                <h2>External Validation Report</h2>
                <br/>
                <div class="rich-panel styleResultBackground">
                    <div class="rich-panel-header">General Informations</div>
                    <div class="rich-panel-body">
                        <table border="0">
                            <tr>
                                <td><b>Validation Date</b></td>
                                <td><xsl:value-of select="detailedResult/ValidationResultsOverview/ValidationDate"/> - <xsl:value-of select="detailedResult/ValidationResultsOverview/ValidationTime"/></td> 
                            </tr>
                            <tr>
                                <td><b>Validation Service</b></td>
                                <td><xsl:value-of select="detailedResult/ValidationResultsOverview/ValidationServiceName"/></td>         
                            </tr>
                            <tr>
                                <td><b>Validation Test Status</b></td>
                                <td>
                                    <xsl:if test="contains(detailedResult/ValidationResultsOverview/ValidationTestResult, 'PASSED')">
                                        <div class="PASSED"><xsl:value-of select="detailedResult/ValidationResultsOverview/ValidationTestResult"/></div>
                                    </xsl:if>
                                    <xsl:if test="contains(detailedResult/ValidationResultsOverview/ValidationTestResult, 'FAILED')">
                                        <div class="FAILED"><xsl:value-of select="detailedResult/ValidationResultsOverview/ValidationTestResult"/></div>
                                    </xsl:if>
                                    <xsl:if test="contains(detailedResult/ValidationResultsOverview/ValidationTestResult, 'ABORTED')">
                                        <div class="ABORTED"><xsl:value-of select="detailedResult/ValidationResultsOverview/ValidationTestResult"/></div>
                                    </xsl:if>
                                </td>
                            </tr>
                        </table>
                    </div>
                </div>
                <br/>
                
                <div class="rich-panel styleResultBackground">
                    <div class="rich-panel-header ">Result overview</div>
                    <div class="rich-panel-body ">
                        <span>
                            <div>
                                <table border="0">
                                    <tbody>
                                        <tr>
                                            <td>
                                                <b>
                                                    <a href="#wellformed">XML</a>
                                                </b>
                                            </td>
                                            <td>
                                                <div>
                                                    <xsl:attribute name="class"><xsl:value-of select="detailedResult/DocumentWellFormed/Result"/></xsl:attribute>
                                                    <xsl:value-of select="detailedResult/DocumentWellFormed/Result"/>
                                                </div>
                                            </td>
                                        </tr>
                                        <xsl:if test="count(detailedResult/DocumentValidXSD) = 1">
                                        <tr>
                                            <td><b><a href="#xsd">XSD</a></b></td>
                                            <td>
                                                <div>
                                                    <xsl:attribute name="class"><xsl:value-of select="detailedResult/DocumentValidXSD/Result"/></xsl:attribute>
                                                    <xsl:value-of select="detailedResult/DocumentValidXSD/Result"/>
                                                </div>
                                            </td>
                                        </tr>
                                        </xsl:if>
                                        <tr>
                                            <td>
                                                <b>
                                                    <a href="#mbv">ModelBased Validation</a>
                                                </b>
                                            </td>
                                            <td>
                                                <div>
                                                    <xsl:attribute name="class"><xsl:value-of select="detailedResult/ValidationResultsOverview/ValidationTestResult"/></xsl:attribute>
                                                    <xsl:value-of select="detailedResult/ValidationResultsOverview/ValidationTestResult"/>
                                                </div>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </span>
                    </div>
                </div>
                <br/>
                
                <div class="rich-panel styleResultBackground">
                    <div class="rich-panel-header "><a name="wellformed">XML Validation Report</a></div>
                    <div class="rich-panel-body ">
                        <span>
                            <div>
                                <xsl:choose>
                                    <xsl:when test="detailedResult/DocumentWellFormed/Result = 'PASSED'">
                                        <p class="PASSED">The XML document is well-formed</p>                        
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <p class="FAILED">The XML document is not well-formed</p>
                                    </xsl:otherwise>
                                </xsl:choose>
                            </div>
                        </span>
                    </div>
                </div>
                <br/>
               
                <xsl:if test="count(detailedResult/DocumentValidXSD) = 1">
                <div class="rich-panel styleResultBackground">
                    <div class="rich-panel-header"><a name="xsd">XSD Validation detailed Results</a></div>
                    <div class="rich-panel-body">
                        <i>The document you have validated is supposed to be an XML document. The validator has checked if it is well-formed and has validated it against one ore several XSD schemas, results of those validations are gathered in this part.</i>
                        <xsl:if test="count(detailedResult/DocumentValidXSD) = 1">
                            <xsl:choose>
                                <xsl:when test="(detailedResult/DocumentValidXSD/Result='PASSED') or (detailedResult/DocumentValidXSD/nbOfErrors=0 and count(detailedResult/DocumentValidXSD/Result)=0)">
                                    <p class="PASSED">The XML document is valid regarding the schema</p>
                                </xsl:when>
                                <xsl:otherwise>
                                    <p class="FAILED">The XML document is not valid regarding the schema because of the following reasons: </p>
                                    <xsl:if test="count(detailedResult/DocumentValidXSD/XSDMessage) &gt; 0">
                                        <ul>
                                            <xsl:for-each select="detailedResult/DocumentValidXSD/XSDMessage">
                                                <xsl:call-template name="viewXSDMess" />
                                            </xsl:for-each>
                                        </ul>
                                    </xsl:if>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:if>
                    </div>
                </div>
                <br/>
                </xsl:if>
                <br/>
                <xsl:if test="count(detailedResult/MDAValidation) = 1">
                <div class="rich-stglpanel" id="resultdetailedann">
                    <div class="rich-stglpanel-header" onclick="hideOrViewValidationDetailsMB();">
                        <a name="mbv">Model Based Validation details</a>
                    </div>
                    <div class="rich-stglpanel-body styleResultBackground" style="display: block;" id="resultdetailedann_body">
                        <table class="styleResultBackground">
                            <tbody>
                                <tr>
                                    <td>
                                        <b>Result</b>
                                    </td>
                                    <td>
                                        <xsl:if test="count(detailedResult/MDAValidation/Error) = 0">
                                            <div class="PASSED">PASSED</div>
                                        </xsl:if>
                                        <xsl:if test="count(detailedResult/MDAValidation/Error) &gt; 0">
                                            <div class="FAILED">FAILED</div>
                                        </xsl:if>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="100px" valign="top">
                                        <b>Summary</b>
                                    </td>
                                    <td>
                                        <xsl:value-of select="count(detailedResult/MDAValidation/Error) + count(detailedResult/MDAValidation/Warning) + count(detailedResult/MDAValidation/Info) + count(detailedResult/MDAValidation/Note)"/> checks <br/>
                                        <xsl:value-of select="count(detailedResult/MDAValidation/Error)"/> errors <br/>
                                        <xsl:value-of select="count(detailedResult/MDAValidation/Warning)"/> warning <br/>
                                        <xsl:value-of select="count(detailedResult/MDAValidation/Info)"/> infos <br/>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <b>HIDE : </b> 
                        <input type="checkbox" onclick="hideOrUnhideMB(this)" name="Errors">Errors</input>
                        <input type="checkbox" onclick="hideOrUnhideMB(this)" name="Warnings">Warnings</input>
                        <input type="checkbox" onclick="hideOrUnhideMB(this)" name="Infos">Infos</input>
                        <input type="checkbox" onclick="hideOrUnhideMB(this)" name="Reports">Reports</input>
                        <xsl:if test="count(detailedResult/MDAValidation/Error) &gt; 0">
                            <div id="Errors_p">
                            <p><b>Errors</b></p>
                            <xsl:for-each select="detailedResult/MDAValidation/Error">
                                <xsl:call-template name="viewnotification">
                                    <xsl:with-param name="kind">Error</xsl:with-param>
                                </xsl:call-template>
                            </xsl:for-each>
                            </div>
                        </xsl:if>
                        <xsl:if test="count(detailedResult/MDAValidation/Warning) &gt; 0">
                            <div id="Warnings_p">
                            <p><b>Warnings</b></p>
                            <xsl:for-each select="detailedResult/MDAValidation/Warning">
                                <xsl:call-template name="viewnotification">
                                    <xsl:with-param name="kind">Warning</xsl:with-param>
                                </xsl:call-template>
                            </xsl:for-each>
                            </div>
                        </xsl:if>
			             <xsl:if test="count(detailedResult/MDAValidation/Info) &gt; 0">
			                 <div id="Infos_p">
                            <p><b>Infos</b></p>
                            <xsl:for-each select="detailedResult/MDAValidation/Info">
                                <xsl:call-template name="viewnotification">
                                    <xsl:with-param name="kind">Note</xsl:with-param>
                                </xsl:call-template>
                            </xsl:for-each>
			                 </div>
                        </xsl:if>
                        <xsl:if test="count(detailedResult/MDAValidation/Note) &gt; 0">
                            <div id="Reports_p">
                            <p><b>Reports</b></p>
                            <xsl:for-each select="detailedResult/MDAValidation/Note">
                                <xsl:if test="position() &lt; 101">
                                    <xsl:call-template name="viewnotification">
                                        <xsl:with-param name="kind">Report</xsl:with-param>
                                    </xsl:call-template>
                                </xsl:if>
                            </xsl:for-each>
                            <xsl:if test="count(detailedResult/MDAValidation/Note) &gt; 100">
                                <table class="Report" width="98%">
                                    <tr>
                                        <td valign="top" width="100"><b>..........</b></td>
                                    </tr>
                                </table>
                                <br/>
                                <table class="moore" width="98%">
                                    <tr>
                                        <td valign="top" width="100"><b>All errors and warnings are shown above. If you want to view the complete report including all positive checks, please download the 'Model Based Validation Result'.</b>
                                            <br/>
                                            <span id="downloadspan"><script type="text/javascript">extractDownloadResultButton()</script></span>
                                        </td>
                                    </tr>
                                </table>
                            </xsl:if>
                            </div>
                        </xsl:if>
                        
                    </div>
                </div>
                    </xsl:if>
            </body>
        </html>
    </xsl:template>
    
    <xsl:template name="viewnotification">
        <xsl:param name="kind"/>
        <table width="98%">
            <xsl:attribute name="class"><xsl:value-of select="$kind"/></xsl:attribute>
            <xsl:if test="count(Test) &gt; 0">
            <tr>
                <td valign="top" width="100"><b>Test</b></td>
                <td><xsl:value-of select="Test"/></td>
            </tr>
            </xsl:if>
            <xsl:if test="count(Location) &gt; 0">
            <tr>
                <td valign="top"><b>Location</b></td>
                <td><xsl:value-of select="Location"/> <xsl:if test="$viewdown = 'true'"><img src="/EVSClient/img/icons64/down.gif" style="vertical-align: middle;" width="15px" onclick="gotoo(this)"/></xsl:if></td>
            </tr>
            </xsl:if>
            
            <tr>
                <td valign="top"><b>Description</b></td>
                <td>
                    <xsl:value-of select="Description"/>
                </td>
            </tr>
        </table>
        <br/>
    </xsl:template>
    
    <xsl:template name="viewXSDMess">
        <xsl:if test="Severity='error'">
            <li><xsl:value-of select="Message"/></li>
        </xsl:if>
    </xsl:template>
</xsl:stylesheet>