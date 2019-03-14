package com.PBRQ.tree;
/**
 * Enumeration of node types.
 * @enum {number}
 */
public enum NodeType {
    EMPTY,	//当前节点不是中间节点，因为刚分裂还没有数据的情况
    LEAF,		//叶子节点，至少有一个数据的叶子结点
    POINTER	//中间节点
}
