import {assert} from "chai"
import {mount} from "@vue/test-utils"
import VInput from "@/components/VInput.vue"

describe("VInput.vue", () => {
    let wrapper
    const value = "my value"
    const name = "my name"
    beforeEach(() => {
        wrapper = mount(VInput, {
            propsData: {
                value: value,
                attributeName: name
            },
        })
    })
    it("props.value is passed to internal input tag", () => {
        const inputWrapper = wrapper.find("input")
        // @ts-ignore
        assert.equal(inputWrapper.element.value, value)
    })
    it("emits a custom event when key is pressed on the input", () => {
        const inputWrapper = wrapper.find("input")
        inputWrapper.trigger("keyup", {
            which: 65
        })
        assert.equal(wrapper.emitted().keyup[0][0].attributeName, name)
        assert.equal(wrapper.emitted().keyup[0][0].value, value)
    })
})
