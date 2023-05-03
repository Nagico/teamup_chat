package cn.nagico.teamup.backend.server

interface NettyServer {
    /**
     * 启动netty
     *
     * @throws InterruptedException
     */
    fun start()

    /**
     * 关闭netty
     */
    fun close()
}