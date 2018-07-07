class BlogsState {
    blogHeaders: Array<BlogHeader> = []
    workingOnBlogOriginal?: BlogDraft
    workingOnBlog?: BlogDraft
}

const getters: GetterTree<MyContentState, any> = {
    blogHeaders(state: MyContentState): Array<BlogHeader> {
    return state.blogHeaders
},
    workingOnBlogOriginal(state: MyContentState): BlogDraft | undefined {
    return state.workingOnBlogOriginal
},
workingOnBlog(state: MyContentState): BlogDraft | undefined {
    return state.workingOnBlog
}
}

const mutations: MutationTree<MyContentState> = {
    setBlogHeaders(state: MyContentState, blogHeaders: Array<BlogHeader>) {
    // return state.blogHeaders
},
workOnBlog(state: MyContentState, blog: BlogDraft) {
    return state.workingOnBlogOriginal
},
}