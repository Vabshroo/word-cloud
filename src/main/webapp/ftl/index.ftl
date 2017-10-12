<html>
<head>
    <meta charset="UTF-8">
    <title>word cloud</title>
    <script src="/js/d3.min.js"></script>
    <script src="/js/d3.layout.cloud.js"></script>
</head>
<body>
    <script type="text/javascript">
        function createRandomItemStyle() {
            return 'rgb(' + [
                        Math.round(Math.random() * 160),
                        Math.round(Math.random() * 160),
                        Math.round(Math.random() * 160)
                    ].join(',') + ')';
        }

        var percent = ${percent};
        if(!percent || percent === null){
            percent = 100;
        }
        percent = percent / 100;
        //var fill = d3.scale.category20();
        //输出20种类别的颜色 ---颜色比例尺

        //word cloud layout  词云布局，d3布局中为词云设立的单独的一种布局方式
        //d3.layout.cloud() 构造一个词云的布局实例
        //on(type,listener) 注册特定的listener来接收布局中特定类型的event。
        //目前只有 word和end这两种event是被支持的。
        //word这种event在该布局完成对每一个word的布局时被调度。
        //end这种活动在改布局完成对所有的words的布局时被调度。
        //注册的listener通过两个参数被调度：
        //被成功布局的单词数组
        //以[{x0,y0},{x1,y1}]形式为界限，代表words范围    a bounds object of the form [{x0, y0}, {x1, y1}] representing the extent of the placed objects.
        //

        //start 布局算法   初始化单词数组上的各种参数，并且从最大的单词开始布局单词，
        //从矩形区域的中间开始，每一个单词在布局时都要检测是否与先前已经布局好的单词位置冲突。
        //一旦检测到冲突，该算法会沿着螺旋线重新布局该单词。
        //如果一个单词不能在沿着螺旋线的任何地方被布局，该单词最终将不会显示在词云图上，该问题可能在后续版本中被解决。

        //stop() 停止布局算法

        // timeInterval([time])   布局时通过setInterval来避免浏览器的event loop被锁住。

        //words([words array].map(function(d)(return{text:d;size:某一个数值}))
        //为words数组中的每一个word分配一个字体大小

        d3.layout.cloud().size([1800, 900]) //size([x,y])  词云显示的大小
        //map 返回新的object数组
                .words(${words})
                //~~的作用是单纯的去掉小数部分，不论正负都不会改变整数部分
                //这里的作用是产生0 1
                .rotate(function() { return (0.5 - Math.random()) * 90 })
                .font("Impact")
                .fontSize(function(d) { return Math.ceil(d.size*percent); })
                .on("end", draw)//结束时运行draw函数
                .start();


        //append()使用函数在指定元素的结尾添加内容
        //transform:translate(x,y)  定义2d旋转，即平移，向右平移x,向下平移y
        function draw(words) {
            d3.select("body").append("svg")
                    .attr("width", 1800)
                    .attr("height", 900)
                    .attr("style","margin:auto")
                    .append("g")
                    .selectAll("text")
                    .data(words)
                    .enter().append("text")
                    .style("border","1px solid blue")
                    .style("font-size", function(d) { return d.size + "px"; })
                    .style("font-family", "Impact")
                    .style("fill", function(d, i) { return createRandomItemStyle(); })//fill 在前面15行定义为颜色集
                    .attr("text-anchor", "middle")
                    .attr("transform", "translate(0900,450)")
                    .transition()
                    .duration(1000)
                    .attr("transform", function(d) {
                        return "translate(" + [d.x+900, d.y+450] + ")rotate(" + d.rotate + ")";
                    })
                    .text(function(d) { return d.text; });


        }

    </script>
</body>
</html>