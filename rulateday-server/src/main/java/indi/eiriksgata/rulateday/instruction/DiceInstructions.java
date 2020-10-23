package indi.eiriksgata.rulateday.instruction;

import indi.eiriksgata.dice.vo.MessageData;
import indi.eiriksgata.dice.config.DiceConfig;
import indi.eiriksgata.dice.exception.DiceInstructException;
import indi.eiriksgata.dice.exception.ExceptionEnum;
import indi.eiriksgata.dice.injection.InstructService;
import indi.eiriksgata.dice.injection.InstructReflex;
import indi.eiriksgata.dice.operation.DiceSet;
import indi.eiriksgata.dice.operation.RollBasics;
import indi.eiriksgata.dice.reply.CustomText;

/**
 * @author: create by Keith
 * @version: v1.0
 * @description: indi.eiriksgata.dice
 * @date:2020/9/24
 **/
@InstructService
public class DiceInstructions {


    @InstructReflex(value = {".ra", ".rc"})
    public String attributeCheck(MessageData data) {


        return "you input .ra";
    }

    @InstructReflex(value = {".st"})
    public String setAttribute(MessageData data) {

        return null;
    }


    @InstructReflex(value = {".r"})
    public String roll(MessageData data) {
        return new RollBasics().rollRandom(data.getMessage(), data.getQqID());
    }


    @InstructReflex(value = {".MessageData", ".set"})
    public String setDiceFace(MessageData data) throws DiceInstructException {
        int setDiceFace = Integer.valueOf(data.getMessage());
        if (setDiceFace > Integer.valueOf(DiceConfig.diceSet.getString("dice.face.max"))) {
            throw new DiceInstructException(ExceptionEnum.DICE_SET_FACE_MAX_ERR);
        }
        if (setDiceFace <= Integer.valueOf(DiceConfig.diceSet.getString("dice.face.min"))) {
            throw new DiceInstructException(ExceptionEnum.DICE_SET_FACE_MIN_ERR);
        }
        new DiceSet().setDiceSet(data.getQqID(), setDiceFace);
        return CustomText.getText("dice.set.face.success", setDiceFace);
    }


}
