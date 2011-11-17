$A.Comment = Ext.extend($A.Component, {
	tpl : ["<li id='{id}_{commentid}' class='comment-li'>", "<div>", "<div class='comment-nick'>",
			"<cite>{author}</cite> <span>留言于：{date}</span></div>",
			"<div class='comment-txt'>{content}</div>", "</div>",
			"<div class='comment-bar'><a href='javascript:$(\"{id}\").remove(\"{commentid}\")'>删除</a></div>", "</div>",
			"</li>"],
	initComponent : function(config) {
		$A.Comment.superclass.initComponent.call(this, config);
		this.txt = $(this.id + "_txt");
		this.count = this.wrap.child("span[atype=count]");
		this.content = this.wrap.child("div.comment-content");
		this.list = this.content.child("ol.comment-list");
	},
	post : function() {
		var textarea = this.txt;
		var txt = textarea.value;
		if (Ext.isEmpty(txt)) {
			$A.showErrorMessage("错误", "请输入评论内容", function() {
				textarea.focus();
			});
			return;
		}
		$A.Masker.mask(this.wrap, "正在发布...");
		$A.request({
			url : this.submiturl + "/insert",
			para : {
				"content" : txt,
				"table_id" : this.tableid,
				"table_name" : this.tablename
			},
			success : this.onPostSuccess,
			failure : this.onPostFailure,
			error : this.onPostFailure,
			scope : this
		})
	},
	remove : function(commentId){
		var sf = this;
		$A.showConfirm("删除","是否确定删除？",function(){
			$A.Masker.mask(sf.wrap, "正在删除...");
			$A.request({
				url : sf.submiturl + "/delete",
				para : {
					"comment_id" : commentId
				},
				success : sf.onRemoveSuccess,
				failure : sf.onPostFailure,
				error : sf.onPostFailure,
				scope : sf
			})
		});
	},
	onPostSuccess : function(res) {
		this.txt.setValue('');
		var r = res.result;
		if (!this.list) {
			this.content.update("<ol class='comment-list'></ol>")
			this.list = this.content.child("ol.comment-list")
		}
		new Ext.Template(this.tpl).append(this.list.dom, {
			"author" : this.username,
			"date" : new Date().format("yyyy年mm月dd日 HH:MM"),
			"content" : r["content"].replace(/&/mg,"&amp;").replace(/</mg,"&lt;").replace(/>/mg,"&gt;"),
			"id" : this.id,
			"commentid" : r["comment_id"]
		});
		this.count.update(Number(this.count.dom.innerHTML)+1);
		$A.Masker.unmask(this.wrap);
	},
	onRemoveSuccess : function(res){
		var id = this.id + "_" + res.result["comment_id"];
		this.count.update(Number(this.count.dom.innerHTML)-1);
		Ext.get(id).remove();
		if(null == this.content.child("li")){
			this.content.update("<p class='comment-li'>暂时没有评论。</p>");
			this.list = null;
		}
		$A.Masker.unmask(this.wrap);
	},
	onPostFailure : function() {
		$A.Masker.unmask(this.wrap);
	}
})