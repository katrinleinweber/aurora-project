Aurora.DateField = Ext.extend(Ext.util.Observable, {
	constructor: function(elId, config) {	
        config = config || {};
        Ext.apply(this, config);
        if(typeof(elId) == "string") Aurora.cmps[elId] = this;
        Aurora.DateField.superclass.constructor.call(this);     

        this.wrap = typeof(elId) == "string" ? Ext.get(elId) : elId;
        this.table = this.wrap.child("table");        
        this.tbody = this.wrap.child("tbody").dom;
        this.initComponent();
        this.initEvents();	
		
		this.draw();
    },
    initComponent : function(){
    	this.days = [];
    	this.selectDays = this.selectDays||[];
    	this.date = this.date||new Date();
		this.year = this.date.getFullYear();
		this.month = this.date.getMonth() + 1;
    	this.preMonthBtn = this.wrap.child("div.item-dateField-pre");
    	this.nextMonthBtn = this.wrap.child("div.item-dateField-next");
    	this.yearSpan = this.wrap.child("span.item-dateField-year");
    	this.monthSpan = this.wrap.child("span.item-dateField-month");    	
    },
    initEvents : function(){   	
    	this.preMonthBtn.on("click", this.preMonth, this);
    	this.nextMonthBtn.on("click", this.nextMonth, this);
    	this.table.on("click", this.onSelect, this);
    	this.table.on("mouseover", this.mouseOver, this);
    	this.table.on("mouseout", this.mouseOut, this)
    	this.addEvents('select');
    },
    mouseOut: function(e){
    	if(this.overTd) Ext.fly(this.overTd).removeClass('dateover');
    },
    mouseOver: function(e){
    	if(this.overTd) Ext.fly(this.overTd).removeClass('dateover');
    	if(Ext.fly(e.target).hasClass('item-day') && e.target.date != 0){
    		this.overTd = e.target; 
    		Ext.fly(this.overTd).addClass('dateover');
    	}
    	
    },
    onSelect: function(e){
    	if(this.singleSelect === false){
    		
    	}else{
    		if(this.selectedDay) Ext.fly(this.selectedDay).removeClass('onSelect');
    		if(Ext.fly(e.target).hasClass('item-day') && e.target.date != 0){
	    		this.selectedDay = e.target; 
	    		this.onSelectDay(this.selectedDay);
	    		this.fireEvent('select', this, this.selectedDay.date);
	    	}
    	}
    },
	onSelectDay: function(o){
		if(!Ext.fly(o).hasClass('onSelect'))Ext.fly(o).addClass('onSelect');
	},
	//在选择日期触发
	onToday: function(o){
		o.className = "onToday";
	},//在当天日期触发
	afterFinish: function(){
		for(var i=0;i<this.selectDays.length;i++){
			var d = this.selectDays[i];
			if(d.getFullYear() == this.year && d.getMonth()+1 == this.month){
				this.onSelectDay(this.days[d.getDate()]);
			}
		}		
	},
    //当前月
	nowMonth: function() {
		this.predraw(new Date());
	},
	//上一月
	preMonth: function() {
		this.predraw(new Date(this.year, this.month - 2, 1));
	},
	//下一月
	nextMonth: function() {
		this.predraw(new Date(this.year, this.month, 1));
	},
	//上一年
	preYear: function() {
		this.predraw(new Date(this.year - 1, this.month - 1, 1));
	},
	//下一年
	nextYear: function() {
		this.predraw(new Date(this.year + 1, this.month - 1, 1));
	},
  	//根据日期画日历
  	predraw: function(date) {
		//再设置属性
		this.year = date.getFullYear(); this.month = date.getMonth() + 1;
		//重新画日历
		this.draw();
  	},
  	//画日历
	draw: function() {
//		return;
		//用来保存日期列表
		var arr = [];
		//用当月第一天在一周中的日期值作为当月离第一天的天数
		for(var i = 1, firstDay = new Date(this.year, this.month - 1, 1).getDay(); i <= firstDay; i++){ 
			arr.push(0); 
		}
		//用当月最后一天在一个月中的日期值作为当月的天数
		for(var i = 1, monthDay = new Date(this.year, this.month, 0).getDate(); i <= monthDay; i++){ 
			arr.push(i); 
		}
		//清空原来的日期对象列表
		this.days = [];
		//先清空内容再插入(ie的table不能用innerHTML)
		while(this.tbody.hasChildNodes()){ 
			this.tbody.removeChild(this.tbody.firstChild); 
		}
		
		//插入日期
//		if(!this.tbody) this.tbody = document.createElement("TBODY");
		while(arr.length){
			//每个星期插入一个tr
			var row = document.createElement("tr");
			//每个星期有7天
			for(var i = 1; i <= 7; i++){
				var cell = document.createElement("td"); 
				cell.className = "item-day";
				cell.innerHTML = "&nbsp;";
				cell.date=0;
				if(arr.length){
					var d = arr.shift();
					if(d){
						cell.innerHTML = d;
						this.days[d] = cell;
						var on = new Date(this.year, this.month - 1, d);
						cell.date=on;
						//判断是否今日
						this.isSame(on, new Date()) && this.onToday(cell);
						//判断是否选择日期
						//this.selectDay && this.isSame(on, this.selectDay) && this.onSelectDay(cell);
					}
				}
				row.appendChild(cell);
			}
			this.tbody.appendChild(row);
		}
		
		
		this.yearSpan.dom.innerHTML = this.year; 
		this.monthSpan.dom.innerHTML = this.month;
		this.afterFinish();
	},
	//判断是否同一日
	isSame: function(d1, d2) {
		return (d1.getFullYear() == d2.getFullYear() && d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate());
	}
});