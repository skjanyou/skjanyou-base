# JavaFx工程计划


## 1、响应式支持
### 1.1、 自定义元素
```
Include 用于替代原来的fx:include，用于导入子页面,并且子页面也支持响应式,通过BuilderFactory来创建
<!-- location只要有setter/getter就可以,<children>需要ObservableList -->
<Include location="sssss">
	<children>
		<TextField fx:id="testText"/>
	</children>
</Include>

*Pane   添加数据绑定操作,添加双向/单向属性,通过BuilderFactory创建Pane的子类,在子类中添加这些响应式元素  ${show}
```