/*
 * Copyright 2016 flipkart.com zjsonpatch.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package com.beyondeye.kjsonpatch;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public abstract class AbstractTest {

    @Parameter
    public PatchTestCase p;

    @Test
    public void test() throws Exception {
        if (p.isOperation()) {
            testOpertaion();
        } else {
            testError();
        }
    }

    private void testOpertaion() throws Exception {
        JsonObject node = p.getNode();

        JsonElement first = node.get("node");
        JsonElement second = node.get("expected");
        JsonElement patch = node.get("op");
        String message = node.has("message") ? node.get("message").toString() : "";

        JsonElement secondPrime = JsonPatch.apply(patch.getAsJsonArray(), first);

        assertThat(message, secondPrime, equalTo(second));
    }

    private void testError() {
        JsonObject node = p.getNode();

        JsonElement first = node.get("node");
        JsonElement patch = node.get("op");

        try {
            JsonPatch.apply(patch.getAsJsonArray(), first);

            fail("Failure expected: " + node.get("message"));
        } catch (Exception ex) {
        }
    }
}
