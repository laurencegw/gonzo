export const isDev = function (): Boolean {
    const dev = process.env.NODE_ENV === "development"
    console.log(`DEV-MODE: ${dev}`)
    return dev
}
